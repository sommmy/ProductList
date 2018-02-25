package ng.com.eliconcepts.productlist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class ListActivity extends AppCompatActivity {

    private RecyclerView productView;
    private List<ListUnit> listUnitList = new ArrayList<>();
    private ListAdapter listAdapter;
    private String pListURL;
    private RequestQueue requestQueue;
    private int page=1;
    private LinearLayoutManager layoutManager;
    private ProgressBar progressBarLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //define views
        productView = findViewById(R.id.productView);
       layoutManager = new LinearLayoutManager(getApplicationContext());
        productView.setLayoutManager(layoutManager);
        progressBarLoading = findViewById(R.id.progressBarLoading);
        //initialize adapter
        listAdapter = new ListAdapter(ListActivity.this,listUnitList);

        //set adapter
        //hide loading bar
        progressBarLoading.setVisibility(View.INVISIBLE);
        productView.setAdapter(listAdapter);

        //add onscroll listener to the recycler view
        productView.addOnScrollListener(prOnScrollListener);

    /*     //a list to be used to populate the recy view
        listUnitList.add(new ListUnit("Sneakers Black and White","3500",R.drawable.snickers,"#00316c","#afd3ff"));
        listUnitList.add(new ListUnit("Round sun glasses dark tint","6500",R.drawable.glass,"#436705","#e5ffb9"));
        listUnitList.add(new ListUnit("Braclet with shinny stars","7000",R.drawable.braclet,"#501508","#ffc6b9"));
        listUnitList.add(new ListUnit("Heels with straps","15000",R.drawable.heels,"#00316c","#afd3ff"));


        //tell adapter we have added more things
        listAdapter.notifyDataSetChanged();*/

        pListURL= Config.SITE_URL + "productfetch.php?page=";


        requestQueue = Volley.newRequestQueue(ListActivity.this);

        //getdata from server
        getData();



//on create ends here
    }

    private RecyclerView.OnScrollListener prOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if(islastItemDisplaying(recyclerView)){
                //so i would call the get data method here
                // show loading progress
                progressBarLoading.setVisibility(View.VISIBLE);
                getData();
                Log.i("ListActivity", "LoadMore");
            }
        }


    };
private boolean islastItemDisplaying(RecyclerView recyclerView){
    //check if the adapter item count is greater than 0
    if(recyclerView.getAdapter().getItemCount() != 0){
     //get the last visible item on screen using the layoutmanager
        int lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
        //apply some logic here.
        if(lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
        return true;
        }

    return  false;
}
    private void getData() {

        //add to requestQueue
        requestQueue.add(getDataFromServer(page));

        //increment page number
        page++;

        //remove any loading progress here
    }

    private JsonArrayRequest getDataFromServer(final int page) {
        //good practice to put a loading progress here

        //Json request begins
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(pListURL + String.valueOf(page),
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
            //this is called when a response is gotten from the server


                //after getting the data, I need to parse it the view
                parseData(response);
                Log.i("URL", pListURL + String.valueOf(page));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //TODO
                    Toast.makeText(ListActivity.this, "time out", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                    Toast.makeText(ListActivity.this, "server error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    //TODO
                    Toast.makeText(ListActivity.this, "network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    //TODO
                    Toast.makeText(ListActivity.this, "parse error", Toast.LENGTH_SHORT).show();
                }
                       /* progressBar.setVisibility(View.GONE); */
                //If an error occurs that means end of the list has reached
                
                Toast.makeText(ListActivity.this, "No More Result Available", Toast.LENGTH_SHORT).show();
            }
        });

        //some retrypoilicy for bad network
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000,0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //return array
        return jsonArrayRequest;
    }

    private void parseData(JSONArray response) {
        Log.i("Response: ", String.valueOf(response));
        //create a forLoop
        for(int i =0; i< response.length(); i++){
            ListUnit listUnit = new ListUnit();
            JSONObject jsonObject = null;
            //because from here they could be failures, so we use try and catch
            try{
                //get json object
                jsonObject = response.getJSONObject(i);
                Log.i("Response: ", String.valueOf(jsonObject));
                //add data from object to objects in ListUnit
                listUnit.setId(jsonObject.getString("id"));
                listUnit.setName(jsonObject.getString("name"));
                listUnit.setPrice(jsonObject.getString("price"));
                listUnit.setImage(jsonObject.getString("image"));
                listUnit.setViewColor(jsonObject.getString("view"));
                listUnit.setColor(jsonObject.getString("color"));

            }catch (JSONException e){
                e.printStackTrace();

            }

            //add all the above to the array list
            listUnitList.add(listUnit);


        }

        //notify the adapter that some things has changed
        listAdapter.notifyDataSetChanged();

progressBarLoading.setVisibility(View.INVISIBLE);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
