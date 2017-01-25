package com.bubble.simplecal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


/**
 * <p>Title: HistoryLayout</p>
 * <p>Description: </p>
 * <p>Company: </p>
 *
 * @author bubble
 * @version 2.0.0.150818
 * @date 2015-7-19 下午2:42:57
 * @since JDK 1.8.0_45
 */
public class HistoryLayout extends LinearLayout implements OnClickListener, OnTouchListener, OnItemClickListener {
    private Context context;
    private final String FILENAME = "history";

    private List<String> equationList = new ArrayList<String>();
    private String hisHistory = "";
    private StringBuilder hisHistorySB = new StringBuilder();
    private ListView hisListView;
    private String[] hisItemArray;
    private String exp;
    private String result;
    private String[] hisItem;
    private EquationAdapter eAdapter;
    private boolean appendHistory;    //是否需要追加history字符串
    private Button clearHisBtn;
    private ClipboardManager clipboard;
    private ClipData clip;
    private String str;
    private Button btn;

    private FileInputStream in;
    private BufferedReader reader;
    private StringBuilder contentSB;
    private String fileDir;
    private File file;

    public HistoryLayout(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.history_layout, this);
        eAdapter = new EquationAdapter(context, R.layout.history_item, equationList);
        hisListView = (ListView) findViewById(R.id.history_list_view);
        hisListView.setAdapter(eAdapter);
        hisListView.setOnItemClickListener(this);

        clearHisBtn = (Button) findViewById(R.id.history_clear);
        clearHisBtn.setOnTouchListener(this);
        clearHisBtn.setOnClickListener(this);
    }

    /* (non-Javadoc)
    * <p>Title: onItemClick</p>
    * <p>Description: 单击复制到剪贴板</p>
    * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
    * @param parent
    * @param view
    * @param position
    * @param id
    * @author bubble
    * @date 2015-7-25 上午10:11:34
    */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        str = parent.getItemAtPosition(position).toString();
        clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clip = ClipData.newPlainText("simple text", str);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
    }

    /**
     * <p>Title: onTouch</p>
     * <p>Description: </p>
     *
     * @param v
     * @param event
     * @return
     * @author bubble
     * @date 2015-7-24 下午11:29:43
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        btn = (Button) v;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                btn.setBackgroundColor(Color.parseColor("#F2F2F2"));
                break;
            case MotionEvent.ACTION_UP:
                btn.setBackgroundColor(Color.WHITE);
                break;
            default:
                break;
        }
        return false;
    }

    /* (non-Javadoc)
     * <p>Title: onClick</p>
     * <p>Description: </p>
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     * @param v
     * @author bubble
     * @date 2015-7-24 下午11:14:21
     */
    @Override
    public void onClick(View v) {
        this.hisHistorySB.setLength(0);
        equationList.clear();
        eAdapter.notifyDataSetChanged();
    }

    /**
     * <p>Title: updateHistory</p>
     * <p>Description: </p>
     *
     * @param calHistory
     * @author bubble
     * @date 2015-7-21 上午12:16:45
     */
    public void updateHistory(String calHistory) {
        hisHistorySB.append(calHistory);
        hisItemArray = calHistory.split(";");
        if (hisItemArray.length > 0) {
            for (String item : hisItemArray) {
                if (!TextUtils.isEmpty(item)) {
                    equationList.add(item.replace(",", "\n"));
                }
            }
            eAdapter.notifyDataSetChanged();
        }
    }

    /**
     * <p>Title: getHistory</p>
     * <p>Description: </p>
     *
     * @return
     * @author bubble
     * @date 2015-7-19
     */
    public String getHistory() {
        return hisHistorySB.toString();
    }

    /**
     * <p>Title: load</p>
     * <p>Description: </p>
     *
     * @return
     * @author bubble
     * @date 2015-7-22 上午9:42:06
     */
    public String load() {
        in = null;
        reader = null;
        contentSB = new StringBuilder();
        fileDir = context.getFilesDir() + File.separator + FILENAME;
        file = new File(fileDir);
        try {
            if (!file.exists())
                return "";
            in = context.getApplicationContext().openFileInput(FILENAME);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                contentSB.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return contentSB.toString();
    }
}