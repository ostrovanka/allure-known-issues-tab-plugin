package org.badger;


import io.qameta.allure.core.LaunchResults;
import io.qameta.allure.entity.Link;
import io.qameta.allure.entity.Status;
import io.qameta.allure.entity.TestResult;
import io.qameta.allure.tree.TestResultTreeGroup;
import io.qameta.allure.tree.Tree;
import io.qameta.allure.tree.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class KnownIssuesPluginTest {

    @Test
    void shouldGroupByIssue() {
        final Set<TestResult> testResults = new HashSet<>();
        testResults.add(new TestResult()
                .setStatus(Status.PASSED)
                .setName("First")
                .setLinks(asList(new Link().setName("123").setType("issue"),
                        new Link().setName("124").setType("issue"))));
        testResults.add(new TestResult()
                .setStatus(Status.PASSED)
                .setName("Second")
                .setLinks(Collections.singletonList(new Link().setName("123").setType("issue"))));
        testResults.add(new TestResult()
                .setStatus(Status.PASSED)
                .setName("Third")
                .setLinks(Collections.singletonList(new Link().setName("124").setType("issue"))));

        LaunchResults results = new DefaultLaunchResults(testResults, Collections.emptyMap(), Collections.emptyMap());

        Tree<TestResult> tree = new KnownIssuesPlugin().getData(Collections.singletonList(results));

        assertThat(tree.getChildren().size()).as("Root node should have 2 child nodes").isEqualTo(2);
        assertThat(tree.getChildren().stream().map(TreeNode::getName).collect(Collectors.toList()))
                .as("Name of root child nodes should be: 123, 124")
                .containsExactlyInAnyOrder("123", "124");
        assertThat(tree.findNodeOfType("123", TestResultTreeGroup.class).get().getChildren())
                .as("Node 123 should have 2 children")
                .hasSize(2)
                .extracting("name").containsExactlyInAnyOrder("First", "Second");
        assertThat(tree.findNodeOfType("124", TestResultTreeGroup.class).get().getChildren())
                .as("Node 124 should have 2 children")
                .hasSize(2)
                .extracting("name").containsExactlyInAnyOrder("First", "Third");

    }
}
