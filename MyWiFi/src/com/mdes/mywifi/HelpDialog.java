package com.mdes.mywifi;

import android.app.AlertDialog;
import android.content.Context;

public class HelpDialog {

	public HelpDialog(Context c){

		try{

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
			alertDialogBuilder.setTitle("Ayuda");

			alertDialogBuilder
			.setMessage("TEXTO")
			.setCancelable(true);

			// crear AlertDialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// mostrar
			alertDialog.show();
		}catch(NullPointerException e){
			LogManager lm = new LogManager(e);
			e.printStackTrace();
		}
	}

}