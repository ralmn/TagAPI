/*
 * Copyright 2012-2014 Matt Baxter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http:www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kitteh.tag;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Here is where the magic is made.
 * <p>
 * This event may fire synchronously, check {@link #isAsynchronous()}
 * <p>
 * Catch this event in order to have an effect on the player's name tag
 */
public final class AsyncPlayerReceiveNameTagEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    /**
     * This is a Bukkit method. Don't touch me.
     *
     * @return registered handlers to Bukkit
     */
    public static HandlerList getHandlerList() {
        return AsyncPlayerReceiveNameTagEvent.handlers;
    }

    private boolean tagModified;
    private boolean uuidModified;
    private final Player named;
    private final Player recipient;
    private String tag;
    private UUID uuid;

    AsyncPlayerReceiveNameTagEvent(Player who, Player named, String initialName, UUID uuid) {
        super(!who.getServer().isPrimaryThread());
        Validate.notNull(who, "Destination player cannot be null!");
        Validate.notNull(named, "Named player cannot be null!");
        Validate.notNull(initialName, "Initial player name cannot be null!");
        this.tagModified = named.getName().equals(initialName);
        this.recipient = who;
        this.named = named;
        this.tag = initialName;
        this.uuid = uuid;
    }

    @Override
    public HandlerList getHandlers() {
        return AsyncPlayerReceiveNameTagEvent.handlers;
    }

    /**
     * Gets the player whose nametag we're receiving
     *
     * @return the Player whose name is being affected
     */
    public Player getNamedPlayer() {
        return this.named;
    }

    /**
     * Gets the player receiving the tag
     *
     * @return the Player receiving the tag
     */
    public final Player getPlayer() {
        return recipient;
    }

    /**
     * Gets the nametag that will be sent
     *
     * @return nametag sent to the player
     */
    public String getTag() {
        return this.tag;
    }

    /**
     * Gets the UUID that will be sent
     * <p>
     * Applies to MC version 1.7 and above
     * <p>
     * In prior versions it's a per-server UUID and not sent to the client
     *
     * @return uuid sent to the player
     */
    public UUID getUUID() {
        return this.uuid;
    }

    /**
     * Gets if the tag has been modified in this event
     *
     * @return true if the tag has been modified
     */
    public boolean isTagModified() {
        return this.tagModified;
    }

    /**
     * Gets if the UUID has been modified in this event
     *
     * @return true if the UUID has been modified
     */
    public boolean isUUIDModified() {
        return this.uuidModified;
    }

    /**
     * Sets the nametag to be sent
     * <p>
     * Will always set the name tag whether returning true or false.
     * <p>
     * Nametags over 16 characters will be truncated
     *
     * @param tag The desired tag
     * @return true if accepted as-is, false if it was truncated
     */
    public boolean setTag(String tag) {
        Validate.notNull(tag, "New nametag cannot be null!");
        if (this.tag.equals(tag)) {
            return true;
        }
        this.tag = tag;
        this.tagModified = true;
        if (tag.length() > 16) {
            tag = tag.substring(0, 16);
            return false;
        }
        return true;
    }

    /**
     * DUE TO MOJANG DESIGN DECISIONS, NO LONGER FUNCTIONS 1.7.6+.
     *
     * @param uuid UUID to be sent
     * @deprecated
     */
    @Deprecated
    public void setUUID(UUID uuid) {
        Validate.notNull(uuid, "New UUID cannot be null!");
        if (this.uuid.equals(uuid)) {
            return;
        }
        this.uuidModified = true;
        this.uuid = uuid;
    }

}