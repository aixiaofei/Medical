package commen;

import java.util.List;

/**
 * Created by ai on 2017/7/9.
 */

public interface CallBackLister {
    List<Docter> process_more_one(String result);
    Docter precess_one(String result);
}
