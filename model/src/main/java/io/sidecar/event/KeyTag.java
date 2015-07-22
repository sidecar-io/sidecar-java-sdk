package io.sidecar.event;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;
import java.util.Set;

public class KeyTag {

    private final String key;
    private final Set<String> tags;

    /**
     * Optional tags that can be added to named reading keys.
     *
     * @param key - The associated key name in the reading
     * @param tags - A list of tags to apply to
     */
    public KeyTag(String key, Set<String> tags) {
        checkNotNull(key);
        this.key = key;
        checkNotNull(tags);
        this.tags = tags;
    }

    /**
     * @return - The name of the key that is being tagged
     */
    public String getKey() {
        return key;
    }

    /**
     *
     * @return - The list of tags for the named reading key
     */
    public Set<String> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyTag keyTag = (KeyTag) o;
        return Objects.equals(key, keyTag.key) &&
                Objects.equals(tags, keyTag.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, tags);
    }

    @Override
    public String toString() {
        return "KeyTag{" +
                "key='" + key + '\'' +
                ", tags=" + tags +
                '}';
    }
}
