package me.latifil.bunkers.profile.manager;

import me.latifil.bunkers.profile.model.Profile;

import java.util.*;
import java.util.stream.Collectors;

public class ProfileManager {

    private final Set<Profile> profiles = new HashSet<>();

    public Set<Profile> getAllPlayingProfiles() {
        return profiles.stream()
                .filter(profile -> profile.getTeam() != null)
                .collect(Collectors.toUnmodifiableSet());
    }

    public Optional<Profile> getProfileByUuid(UUID uuid) {
        return profiles.stream()
                .filter(profile -> profile.getId().equals(uuid))
                .findFirst();
    }

    public void addProfile(Profile profile) {
        profiles.add(profile);
    }

    public void removeProfile(Profile profile) {
        profiles.remove(profile);
    }
}
