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
        for (int i = 0; i < depth; i++) {
            stars.append("*");
        }
        result.append(stars).append(name).append("::").append(id);
        for (StringID item : items) {
            result.append(item.toString(depth + 1));
        }
        return result.toString();
    }
}
