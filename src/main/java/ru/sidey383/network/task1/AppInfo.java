package ru.sidey383.network.task1;

import java.util.UUID;

public record AppInfo(UUID uuid, String host) {
    @Override
    public String toString() {
        return host + ':' + uuid;
    }
}
