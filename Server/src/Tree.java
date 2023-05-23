
import java.util.ArrayList;
import java.util.List;

public class Tree {
    public List<CustomString> items = new ArrayList<>();

    public Tree() {
    }

    public String toSend() {
        StringBuilder result = new StringBuilder();
        for (CustomString item : items) {
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
                CustomString customString = new CustomString(name, id);
                if (depth == 1) {
                    tree.items.add(customString);
                } else {
                    CustomString parent = findParent(tree.items.get(tree.items.size() - 1), depth - 1);
                    parent.addItem(customString);
                }
            }
        }
        return tree;
    }

    private static CustomString findParent(CustomString item, int depth) {
        if (depth == 1) {
            return item;
        } else {
            return findParent(item.items.get(item.items.size() - 1), depth - 1);
        }
    }
}

class CustomString {
    public String name;
    public int id;
    public List<CustomString> items = new ArrayList<>();

    public CustomString(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public void addItem(CustomString newItem) {
        items.add(newItem);
    }

    public String toString(int depth) {
        StringBuilder result = new StringBuilder();
        StringBuilder stars = new StringBuilder();
        result.append("*").append(depth).append(name).append("::").append(id);
        for (CustomString item : items) {
            result.append(item.toString(depth + 1));
        }
        return result.toString();
    }
}
