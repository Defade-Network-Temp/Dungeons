package net.defade.dungeons.map;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;

import java.util.List;

public record Room(Component name, List<Pos> spawningPos) { }
