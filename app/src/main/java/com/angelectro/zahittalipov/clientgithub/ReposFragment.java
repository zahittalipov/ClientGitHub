package com.angelectro.zahittalipov.clientgithub;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.angelectro.zahittalipov.clientgithub.entity.Commit;
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
@EFragment(R.layout.single_repos)
public class ReposFragment extends Fragment {
    Repos repos = null;
    @ViewById(R.id.sName)
    TextView nameTV;
    @ViewById(R.id.sLogin)
    TextView loginTV;
    @ViewById(R.id.sDesc)
    TextView descriptionTV;
    @ViewById(R.id.swatchers)
    TextView watchersTV;
    @ViewById(R.id.sforks)
    TextView forksTV;
    CommitAdapter commitAdapter = null;
    @ViewById(R.id.sCommits)
    ListView listView;
    @ViewById(R.id.sAvatar)
    ImageView avatar;
    @ViewById(R.id.commitProgressBar)
    ProgressBar progressBar;
    @ViewById(R.id.errorLayoutSingleRepos)
    LinearLayout layoutError;
    int progressState=View.VISIBLE;

    List<Commit> commits = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        repos = (Repos) this.getArguments().getSerializable("repos");
        this.getArguments().remove("repos");
        commits = new ArrayList<>();
        commitAdapter = new CommitAdapter(getActivity().getApplicationContext(), commits);
        updateRepos();

    }

    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(progressState);
        layoutError.setVisibility(View.GONE);
        Log.d("saef", repos.getFullName());
        loginTV.setText(repos.getOwner().getLogin());
        nameTV.setText(repos.getName());
        descriptionTV.setText(repos.getDescription());
        forksTV.setText(repos.getForks_count() + "");
        watchersTV.setText(repos.getWatchers_count() + "");
        listView.setAdapter(commitAdapter);
        DownloadImage image = new DownloadImage();
        image.execute(repos.getOwner().getmAvatarUrl(), avatar);
    }

    void updateRepos() {
        Call<List<Commit>> call = ApiGitHub.getAPI(getActivity().getApplicationContext()).getCommits(repos.getOwner().getLogin(), repos.getName());
        call.enqueue(new Callback<List<Commit>>() {
            @Override
            public void onResponse(Response<List<Commit>> response, Retrofit retrofit) {
                Log.d("count_commit", response.raw().request().urlString());
                commits.addAll(response.body());
                commitAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                progressState=View.GONE;
            }

            @Override
            public void onFailure(Throwable t) {
                progressBar.setVisibility(View.GONE);
                layoutError.setVisibility(View.VISIBLE);
            }
        });
    }

    @Click(R.id.buttonUpdate)
    public void clickUpdate() {
        updateRepos();
        progressBar.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
    }

    class CommitAdapter extends BaseAdapter {
        private List<Commit> commits;
        private Context context;
        private LayoutInflater inflater;

        public CommitAdapter(Context context, List<Commit> commits) {
            this.commits = commits;
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return commits.size();
        }

        @Override
        public Object getItem(int position) {
            return commits.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            Commit commit = commits.get(position);
            if (view == null) {
                view = inflater.inflate(R.layout.item_commit, parent, false);
                TextView message = (TextView) view.findViewById(R.id.cText);
                TextView login = (TextView) view.findViewById(R.id.cLogin);
                TextView hash = (TextView) view.findViewById(R.id.cHash);
                TextView date = (TextView) view.findViewById(R.id.cDate);
                message.setText(commit.getCommit().getMessage());
                login.setText(commit.getCommit().getAuthor().getName());
                hash.setText(commit.getSha());
                date.setText(commit.getCommit().getAuthor().getDate());
            }
            return view;
        }
    }

}
