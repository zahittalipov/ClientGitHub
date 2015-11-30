package com.angelectro.zahittalipov.clientgithub;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.angelectro.zahittalipov.clientgithub.entity.Repos;
import com.angelectro.zahittalipov.clientgithub.inteface.ApiGitHub;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Zahit Talipov on 24.11.2015.
 */
@EFragment(R.layout.list_repos)
public class ListReposFragment extends Fragment implements AdapterView.OnItemClickListener {
    @ViewById(R.id.reposListView)
    ListView listView;
    @ViewById(R.id.listReposProgressBar)
    ProgressBar progressBar;
    ReposAdapter adapter = null;
    List<Repos> reposes;
    @ViewById(R.id.errorLayout)
    LinearLayout errorLayout;
    int progressState = View.VISIBLE;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onStart() {
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        progressBar.setVisibility(progressState);
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        reposes = new ArrayList<>();
        adapter = new ReposAdapter(getActivity().getApplicationContext(), reposes);
        updateList();
    }

    public void updateList() {
        Callback<List<Repos>> callback = new Callback<List<Repos>>() {
            @Override
            public void onResponse(Response<List<Repos>> response, Retrofit retrofit) {
                Log.d("count", response.body().size() + "");
                reposes.addAll(response.body());
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                progressState=View.GONE;
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("error", "error");
                progressBar.setVisibility(View.INVISIBLE);
                errorLayout.setVisibility(View.VISIBLE);

            }
        };
        Call<List<Repos>> observable = ApiGitHub.getAPI(getActivity().getApplicationContext()).reposUser();
        observable.enqueue(callback);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Repos repos = (Repos) adapter.getItem(position);
        ReposFragment fragment = new ReposFragment_();
        fragment.setRetainInstance(true);
        Bundle bundle = new Bundle();
        bundle.putSerializable("repos", repos);
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.frameLayoutMy, fragment, "fragment").addToBackStack(null).commit();
    }

    @Click(R.id.buttonUpdate)
    public void clickUpdate() {
        updateList();
        progressBar.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
    }

    class ReposAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        List<Repos> reposes;


        public ReposAdapter(Context context, List<Repos> reposes) {
            this.context = context;
            this.reposes = reposes;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return reposes.size();
        }

        @Override
        public Object getItem(int position) {
            return reposes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return reposes.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.item_list_repos, parent, false);
            }

            Repos repos = reposes.get(position);
            view.setId(repos.getId());
            ((TextView) view.findViewById(R.id.name_repos)).setText(repos.getName());
            return view;
        }
    }
}
