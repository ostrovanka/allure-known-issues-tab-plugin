package org.badger;

import io.qameta.allure.CommonJsonAggregator;
import io.qameta.allure.core.LaunchResults;
import io.qameta.allure.entity.Link;
import io.qameta.allure.entity.TestResult;
import io.qameta.allure.tree.DefaultTreeLayer;
import io.qameta.allure.tree.Tree;
import io.qameta.allure.tree.TreeLayer;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.qameta.allure.entity.TestResult.comparingByTimeAsc;

public class KnownIssuesPlugin extends CommonJsonAggregator {

    protected static final String ISSUES = "issues";
    protected static final String JSON_FILE_NAME = "issues.json";
    protected static final String TYPE_ISSUE = "issue";

    public KnownIssuesPlugin() {
        super(JSON_FILE_NAME);
    }

    public static List<TreeLayer> groupByIssues(final TestResult testResult) {
        return collectIssues(testResult)
                .stream()
                .map(DefaultTreeLayer::new)
                .collect(Collectors.toList());
    }

    private static List<String> collectIssues(final TestResult testResult) {
        return testResult.getLinks().stream()
                .filter(isIssue())
                .map(Link::getName)
                .collect(Collectors.toList());
    }

    private static Predicate<Link> isIssue() {
        return link -> link.getType().equals(TYPE_ISSUE);
    }

    @Override
    public Tree<TestResult> getData(final List<LaunchResults> launchResults) {
        final Tree<TestResult> issues = new IssuesTree(ISSUES, KnownIssuesPlugin::groupByIssues);

        launchResults.stream()
                .map(LaunchResults::getResults)
                .flatMap(Collection::stream)
                .filter(t -> t.getLinks().stream().anyMatch(isIssue()))
                .sorted(comparingByTimeAsc())
                .forEach(issues::add);
        return issues;
    }

}
