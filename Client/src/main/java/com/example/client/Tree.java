package com.example.client;

import java.util.ArrayList;
import java.util.List;

public class Tree {
    public List<StringID> items = new ArrayList<>();

    public Tree() {
    }

    public String toSend() {
        StringBuilder result = new StringBuilder();
        for (StringID item : items) {
            result.append(item.toString(1));
        }
        return result.toString().replaceAll("\\R", "");
    }

    public static Tree fromSend(String treeString) {
        Tree tree = new Tree();
        String[] lines = treeString.split("\\*");
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                int depth = Character.getNumericValue(line.charAt(0));
                String[] parts = line.split("::");
                String name = parts[0].substring(1);
                int id = Integer.parseInt(parts[1]);
                StringID stringID = new StringID(name, id);
                if (depth == 1) {
                    tree.items.add(stringID);
                } else {
                    StringID parent = findParent(tree.items.get(tree.items.size() - 1), depth - 1);
                    parent.addItem(stringID);
                }
            }
        }
        return tree;
    }

    private static StringID findParent(StringID item, int depth) {
        if (depth == 1) {
            return item;
        } else {
            return findParent(item.items.get(item.items.size() - 1), depth - 1);
        }
    }
}

class StringID {
    public String name;
    public int id;
    public List<StringID> items = new ArrayList<>();

    public StringID(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public void addItem(StringID newItem) {
        items.add(newItem);
    }

    public String toString(int depth) {
        StringBuilder result = new StringBuilder();
        StringBuilder stars = new StringBuilder();
        result.append("*").append(depth).append(name).append("::").append(id);
        for (StringID item : items) {
            result.append(item.toString(depth + 1));
        }
        return result.toString();
    }
}
