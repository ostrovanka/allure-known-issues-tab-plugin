package org.badger;

import io.qameta.allure.entity.TestResult;
import io.qameta.allure.tree.*;

import java.util.Collections;
import java.util.List;

import static io.qameta.allure.tree.TreeUtils.createGroupUid;

public class IssuesTree implements Tree<TestResult> {
    protected final TestResultTreeGroup root;
    protected final TreeClassifier<TestResult> treeClassifier;
    protected final TreeLeafFactory<TestResult, TestResultTreeGroup, TestResultTreeLeaf> leafFactory;
    protected final TreeGroupFactory<TestResult, TestResultTreeGroup> treeGroupFactory;

    public IssuesTree(final String name, final TreeClassifier<TestResult> treeClassifier) {
        this(name, treeClassifier, new TestResultGroupFactory(), new TestResultLeafFactory());
    }

    public IssuesTree(final String name, final TreeClassifier<TestResult> treeClassifier,
                      final TreeGroupFactory<TestResult, TestResultTreeGroup> groupFactory,
                      final TreeLeafFactory<TestResult, TestResultTreeGroup, TestResultTreeLeaf> leafFactory) {
        this.root = new TestResultTreeGroup(createGroupUid(null, name), name);
        this.leafFactory = leafFactory;
        this.treeGroupFactory = groupFactory;
        this.treeClassifier = treeClassifier;
    }


    @Override
    public void add(final TestResult item) {
        treeClassifier.classify(item).forEach(layer ->
                layer.getGroupNames()
                        .forEach(name -> {
                            final TestResultTreeGroup parent = getNodeByName(name, item);
                            final TestResultTreeLeaf leaf = leafFactory.create(parent, item);
                            parent.addChild(leaf);
                        })
        );
    }

    private TestResultTreeGroup getNodeByName(String name, TestResult item) {
        return root.findNodeOfType(name, getRootType())
                .orElseGet(() -> {
                    final TestResultTreeGroup created = treeGroupFactory.create(root, name, item);
                    root.addChild(created);
                    return created;
                });
    }

    protected Class<TestResultTreeGroup> getRootType() {
        return TestResultTreeGroup.class;
    }

    @Override
    public List<TreeNode> getChildren() {
        return Collections.unmodifiableList(root.getChildren());
    }

    @Override
    public void addChild(TreeNode node) {
        root.addChild(node);
    }

    @Override
    public String getName() {
        return root.getName();
    }
}
