package course.labs.asynctasklab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

public class DownloaderTaskFragment extends Fragment {

	private DownloadFinishedListener mCallback;
	private Context mContext;
	
	@SuppressWarnings ("unused")
	private static final String TAG = "Lab-Threads";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Preserve across reconfigurations
		setRetainInstance(true);

        DownloaderTask downloaderTask = new DownloaderTask();

		Bundle bundle = getArguments();
		ArrayList<Integer> ids = bundle.getIntegerArrayList(MainActivity.TAG_FRIEND_RES_IDS);
		
		downloaderTask.execute(ids.toArray(new Integer[0]));

	}

	// Связываем текущий родительский Activity с mCallback
	// Сохраняем контекст приложения для использования в downloadTweets()
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		mContext = activity.getApplicationContext(); 

		// Убеждаемся, что родительское активити реализует требуемый интерфейс
		try {
			mCallback = (DownloadFinishedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " должен реализовывать DownloadFinishedListener");
		}
	}

	// Обнуляем mCallback
	@Override
	public void onDetach() {
		super.onDetach();
		mCallback = null;
	}


		public class DownloaderTask extends AsyncTask<Integer, Void, String[]> {

			@Override
			protected String[] doInBackground(Integer... params) {
				return downloadTweets(params);
			}

			@Override
			protected void onPostExecute(String[] strings) {
				((MainActivity)getActivity()).notifyDataRefreshed(strings);
			}
		}


         private String[] downloadTweets(Integer resourceIDS[]) {
			final int simulatedDelay = 2000;
			String[] feeds = new String[resourceIDS.length];
			try {
				for (int idx = 0; idx < resourceIDS.length; idx++) {
					InputStream inputStream;
					BufferedReader in;
					try {
						// Притворяемся, что скачивание занимает долгое время
						Thread.sleep(simulatedDelay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					inputStream = mContext.getResources().openRawResource(
							resourceIDS[idx]);
					in = new BufferedReader(new InputStreamReader(inputStream));

					String readLine;
					StringBuffer buf = new StringBuffer();

					while ((readLine = in.readLine()) != null) {
						buf.append(readLine);
					}

					feeds[idx] = buf.toString();

					if (null != in) {
						in.close();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return feeds;
		}



    
    
    
    
    
    

}