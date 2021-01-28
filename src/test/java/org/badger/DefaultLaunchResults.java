package org.badger;

import io.qameta.allure.core.LaunchResults;
import io.qameta.allure.entity.Attachment;
import io.qameta.allure.entity.TestResult;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


public class DefaultLaunchResults implements LaunchResults {

    private final Set<TestResult> results;

    private final Map<Path, Attachment> attachments;

    private final Map<String, Object> extra;

    public DefaultLaunchResults(final Set<TestResult> results,
                                final Map<Path, Attachment> attachments,
                                final Map<String, Object> extra) {
        this.results = results;
        this.attachments = attachments;
        this.extra = extra;
    }

    @Override
    public Set<TestResult> getAllResults() {
        return results;
    }

    @Override
    public Map<Path, Attachment> getAttachments() {
        return attachments;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getExtra(final String name) {
        return Optional.ofNullable((T) extra.get(name));
    }
}
