package net.defade.dungeons.difficulty;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public enum Difficulty {
    NORMAL(Component.text("Normal").color(TextColor.color(0, 170, 0)).decoration(TextDecoration.ITALIC, false)),
    HARD(Component.text("Hard").color(TextColor.color(220, 110, 30)).decoration(TextDecoration.ITALIC, false)),
    INSANE(Component.text("Insane").color(TextColor.color(170, 30, 10)).decoration(TextDecoration.ITALIC, false));

    private final Component name;

    Difficulty(Component name) {
        this.name = name;
    }

    public Component getName() {
        return name;
    }
}
