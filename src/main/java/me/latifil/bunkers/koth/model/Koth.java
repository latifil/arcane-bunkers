package me.latifil.bunkers.koth.model;

import me.latifil.bunkers.profile.model.Profile;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class Koth {
    private String name;
    private Profile capper;
    private int time;
    private int maxCapTime;
    private final Queue<Profile> capQueue;

    public Koth(final String name, final int time) {
        this.name = Objects.requireNonNull(name, "name");
        this.time = time;
        this.maxCapTime = time;
        this.capper = null;
        this.capQueue = new LinkedList<>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    public Profile getCapper() {
        return this.capper;
    }

    public void setCapper(final Profile capper) {
        this.capper = capper;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(final int time) {
        this.time = time;
    }

    public void setMaxCapTime(final int maxCapTime) {
        this.maxCapTime = maxCapTime;
    }

    public int getMaxCapTime() {
        return this.maxCapTime;
    }

    public Queue<Profile> getCapQueue() {
        return this.capQueue;
    }

    public void enqueueCapper(final Profile profile) {
        this.capQueue.offer(profile);
    }

    public Profile dequeueCapper() {
        return this.capQueue.poll();
    }
}
