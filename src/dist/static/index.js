allure.api.addTab('knownIssues', {
    title: 'Issues', icon: 'fa fa-bug',
    route: 'knownIssues(/)(:testGroup)(/)(:testResult)(/)(:testResultTab)(/)',
    onEnter: (
        function(testGroup, testResult, testResultTab) {
            return new allure.components.TreeLayout({
                testGroup: testGroup,
                testResult: testResult,
                testResultTab: testResultTab,
                tabName: 'Issues',
                baseUrl: 'knownIssues',
                url: 'data/issues.json'
            });
        }
    )
});