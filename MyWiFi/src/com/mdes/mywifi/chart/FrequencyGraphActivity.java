package com.mdes.mywifi.chart;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.WindowManager;

import com.mdes.mywifi.WifiMap;
import com.mdes.mywifi.WifiThread;
import com.mdes.mywifi.broadcastreceiver.WifiChangeReceiver;
import com.mdes.mywifi.broadcastreceiver.WifiNotFoundReceiver;

public class FrequencyGraphActivity extends Activity {

	public static GraphicalView view;
	public static XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	public static XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	private WifiChangeReceiver wifiReceiver = new WifiChangeReceiver();
	private GraphicalView mChartView;
	private Context c;
	private BroadcastReceiver currentActivityReceiver;
	private WifiNotFoundReceiver wifiNotFoundReceiver = new WifiNotFoundReceiver();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//		try{
		registerReceivers();
		rendererSetUp();

		super.onCreate(savedInstanceState);

		//Quitar t�tulo de la actividad y pantalla completa
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		view = ChartFactory.getLineChartView(this, FrequencyGraphActivity.mDataset, FrequencyGraphActivity.mRenderer);		
		setContentView(view);
		currentActivityReceiver = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				rendererSetUp();
				if(WifiMap.getMaxLevel() > -50){
					mRenderer.setYAxisMax((WifiMap.getMaxLevel()/10)*10+10);
				}
				view.repaint();
			}

		};
	}

	protected void onStart() {
		super.onStart();
		WifiThread.isFreqGraph = true;
		c = this;
				rendererSetUp();
				view = ChartFactory.getLineChartView(this, mDataset, mRenderer);	
				setContentView(view);
		registerReceivers();
	}

	@Override
	protected void onPause() {
		WifiThread.isFreqGraph = false;
		super.onPause();
		unregisterReceivers();
//		MultipleGraph.deleteFreqGraph();
	}

	@Override
	protected void onResume() {
		WifiThread.isFreqGraph = true;
//		MultipleGraph.createFreqGraph();
		super.onResume();
		registerReceivers();
	}


	private void rendererSetUp(){
				mRenderer.setClickEnabled(false);
				mRenderer.setShowGrid(true);
				mRenderer.setApplyBackgroundColor(true);
				mRenderer.setBackgroundColor(Color.BLACK);
				mRenderer.setAxisTitleTextSize(20);
				mRenderer.setChartTitleTextSize(27);
				mRenderer.setChartTitle("Gr�fica de transmisi�n por canales");
				mRenderer.setLabelsTextSize(20);
				mRenderer.setLegendTextSize(20);
				mRenderer.setYTitle("Potencia [dBm]");
				mRenderer.setXTitle("Canal");
				mRenderer.setMargins(new int[] { 50, 40, 10, 30 });
				mRenderer.setXAxisMax(12);
				mRenderer.setXAxisMin(0);
				mRenderer.setYLabels(11);
				mRenderer.setXLabels(11);
				mRenderer.setYAxisMax(-50);
				mRenderer.setYAxisMin(-120);
	}

	private void registerReceivers(){
		registerReceiver(wifiReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
		registerReceiver(currentActivityReceiver, new IntentFilter("com.mdes.mywifi.timer"));
		registerReceiver(wifiNotFoundReceiver, new IntentFilter("com.mdes.mywifi.wifinotfound"));
		registerReceiver(wifiNotFoundReceiver, new IntentFilter("com.mdes.mywifi.wififound"));
	}

	private void unregisterReceivers(){
		unregisterReceiver(wifiReceiver);
		unregisterReceiver(wifiNotFoundReceiver);
		unregisterReceiver(currentActivityReceiver);
	}



}