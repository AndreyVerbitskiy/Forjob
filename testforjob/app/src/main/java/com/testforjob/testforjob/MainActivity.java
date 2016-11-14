package com.testforjob.testforjob;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public Elements content;
    public Elements href;
    public ArrayList<String> titleList = new ArrayList<String>();
    public ArrayList<String> linkList = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.list);
        new NewThread().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                int idClicked = position;
                String selectLink = linkList.get(idClicked);
                Toast.makeText(getApplicationContext(), selectLink,
                        Toast.LENGTH_LONG).show();
                Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectLink));
                startActivity(browseIntent);
            }
        });

        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.pro_item, titleList);
    }


        public class NewThread extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... arg) {
                Document doc;
                try {
                    doc = Jsoup.connect("http://4pda.ru/news/").get();
                    content = doc.select(".list-post-title");
                    href = doc.select(".list-post-title a");
                    titleList.clear();
                    linkList.clear();


                    for (Element contents : content) {
                        titleList.add(contents.text());
                    }
                    // Ищем ссылки
                    for (Element hrefs : href) {
                        // записываем в аррей лист
                        linkList.add(hrefs.attr("abs:href"));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                lv.setAdapter(adapter);
            }

        }

    }







