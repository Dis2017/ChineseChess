package chinesechess.game.disstudio.top.chinesechess.Bean;

import java.util.ArrayList;

public class RecordList extends ArrayList<Record> {

    public Record Get() {
        if (size() > 0) {
            return get(0);
        }
        return null;
    }

    public void Add(Record record) {
        add(0, record);
    }

    public void Remove() {
        remove(0);
    }

}
