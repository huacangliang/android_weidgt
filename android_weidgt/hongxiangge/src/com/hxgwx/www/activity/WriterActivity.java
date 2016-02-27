package com.hxgwx.www.activity;

import java.util.ArrayList;
import java.util.List;

import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.R;
import com.hxgwx.www.adapter.ImagesAdapter;
import com.hxgwx.www.bean.ArticTypeBaseList;
import com.hxgwx.www.bean.BaseBean;
import com.hxgwx.www.bean.SendARCBody;
import com.hxgwx.www.bean.SendARCBodyImages;
import com.hxgwx.www.db.ArcMoelDBhelper;
import com.hxgwx.www.fragment.MainListFragment;
import com.hxgwx.www.utils.ActivityManagerUtils;
import com.hxgwx.www.utils.SystemBarTintManager;
import com.hxgwx.www.utils.UploadImageUtils;
import com.hxgwx.www.web.WebPlus;
import com.hxgwx.www.webimpl.WebPlusImpl;
import com.jingzhong.asyntask2.Asyntask2;
import com.jingzhong.asyntask2.annotation.CreateView;
import com.jingzhong.asyntask2.utils.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class WriterActivity
		extends Activity
		implements ActivityManagerUtils {

	private int						fromLgon;

	private PopupWindow				pWindow;

	private View					baseView;

	private String					typeId;

	private String					typeStr;

	@CreateView("bt_type")
	private Button					bt_type;

	@CreateView("et_title")
	private EditText				et_title;

	@CreateView("et_content")
	private EditText				et_content;

	@CreateView("bt_send")
	private Button					bt_send;

	@CreateView("bt_clear")
	private Button					bt_clear;

	@CreateView("bt_typeSeting")
	private Button					bt_typeSeting;

	@CreateView("bt_save")
	private Button					bt_save;

	@CreateView("gv_images")
	private GridView				gv_images;

	private String					title;

	private String					content;

	private WebPlus					web;

	private SendARCBody				body	= new SendARCBody();

	private List<String>			typeIds	= new ArrayList<String>();

	private List<SendARCBodyImages>	images	= new ArrayList<SendARCBodyImages>();

	private HongxianggeApplication	application;

	public void onBack(View v) {
		// TODO Auto-generated method stub
		finish();
	}

	private void showType() {

		et_content.setEnabled(false);
		et_title.setEnabled(false);
		bt_clear.setEnabled(false);
		bt_save.setEnabled(false);
		bt_send.setEnabled(false);
		bt_type.setEnabled(false);
		bt_typeSeting.setEnabled(false);

		if (pWindow != null) {
			pWindow.showAtLocation(baseView, Gravity.CENTER, 0, 0);
			return;
		}

		for (int i = 0; i < MainListFragment.typeList.size(); i++) {
			ArticTypeBaseList type = MainListFragment.typeList.get(i);
			if (type.getIssend().equals("1")) {
				String key = type.getId();
				typeIds.add(key);
			}

		}

		pWindow = new PopupWindow();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
		int mScreenHeight = dm.heightPixels;

		pWindow.setWidth(mScreenWidth / 2);
		pWindow.setHeight(mScreenHeight / 2);

		final View view = LinearLayout.inflate(this, R.layout.arc_types, null);
		ListView lv = (ListView) view.findViewById(R.id.lv_types);
		pWindow.setContentView(view);
		pWindow.setAnimationStyle(android.R.anim.fade_in);
		pWindow.showAtLocation(baseView, Gravity.CENTER, 0, 0);
		pWindow.setOutsideTouchable(true);
		pWindow.setFocusable(false);
		lv.setAdapter(new typeAdapter());
		pWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				et_content.setEnabled(true);
				et_title.setEnabled(true);
				bt_clear.setEnabled(true);
				bt_save.setEnabled(true);
				bt_send.setEnabled(true);
				bt_type.setEnabled(true);
				bt_typeSeting.setEnabled(true);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (pWindow != null && pWindow.isShowing()) {
				pWindow.dismiss();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private class typeAdapter
			extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return typeIds.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return typeIds.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = LinearLayout.inflate(getApplicationContext(), R.layout.types_item, null);
			TextView tv = (TextView) convertView.findViewById(R.id.tv_type);
			final String k = typeIds.get(position);
			final String vd = MainListFragment.TYPES.get(k);
			tv.setText(vd);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					typeId = k;
					typeStr = vd;
					pWindow.dismiss();
					bt_type.setText(typeStr);
				}
			});
			return convertView;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		add();
		baseView = LinearLayout.inflate(this, R.layout.artic_writer, null);
		setContentView(baseView);
		SystemBarTintManager.init(this);
		fromLgon = getIntent().getIntExtra("from", -1);
		web = new WebPlusImpl(this);
		application = (HongxianggeApplication) getApplication();
		try {
			Utils.injectObject(this, this, R.id.class);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		init();
		
		PushAgent.getInstance(this).onAppStart();
	}

	sendAsy					send	= new sendAsy();

	private ArcMoelDBhelper	adb;

	private ImagesAdapter	iAdapter;

	private void init() {
		iAdapter = new ImagesAdapter(images, this);
		adb = ArcMoelDBhelper.getInstance(this);
		gv_images.setAdapter(iAdapter);
		gv_images.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				SendARCBodyImages key = (SendARCBodyImages) parent.getItemAtPosition(position);
				images.remove(key);
				body.getImages(false).remove(key);
				iAdapter.notifyDataSetChanged();
			}
		});
		if (fromLgon == 1) {
			if (com.hxgwx.www.utils.Utils.checkLogin(application)) {
				body = (SendARCBody) getIntent().getSerializableExtra("body");
				typeId = body.getTypeid();
				typeStr = MainListFragment.TYPES.get(body.getTypeid());
				et_title.setText(body.getTitle());
				et_content.setText(body.getBody());
				bt_type.setText(typeStr);
				images.addAll(body.getImages(true));
				iAdapter.notifyDataSetChanged();
				body.delete(adb);
			}
			else {
				Utils.showLongToast(getApplicationContext(), "请登录");
			}
		}

		bt_type.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showType();
			}
		});
		bt_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (pWindow != null && pWindow.isShowing()) return;

				title = et_title.getText().toString();
				content = et_content.getText().toString();
				if (TextUtils.isEmpty(title) || TextUtils.isEmpty(typeId) || TextUtils.isEmpty(content)) {
					com.hxgwx.www.utils.Utils.showLongToast(application, "数据输入不完整");
					return;
				}
				content = com.hxgwx.www.utils.Utils.defaultTyepSetting(content);
				showCheck();
				send.execute("");
			}
		});

		bt_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (pWindow != null && pWindow.isShowing()) return;
				et_content.setText("");
				content = "";
			}
		});

		et_content.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_SPACE) {
					String text = et_content.getText().toString();
					int index = et_content.getSelectionStart();
					if (index < text.length()) {
						String nText = text.substring(0, index);
						nText += "\t";
						text = text.substring(index + 1, text.length());
						nText += text;
						et_content.setText(nText);
					}
					else {
						text += "\t";
						et_content.setText(text);
					}
					return true;
				}
				return false;
			}
		});

		bt_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				save();
			}
		});
		bt_typeSeting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// if (pWindow != null && pWindow.isShowing())
				// return;
				//
				// if (bt_typeSeting.getText().toString().equals("排版")) {
				//
				// String t = et_content.getText().toString();
				// if (TextUtils.isEmpty(t))
				// return;
				// tempContent = t;
				// t = com.hxgwx.www.utils.Utils.tyepSetting(t);
				// et_content.setText(t);
				// bt_typeSeting.setText("取消排版");
				// et_content.setSelection(t.length());
				// } else {
				// showPromptSettingType();
				// }
				if (images != null && images.size() >= 4) {
					Utils.showShortToast(getApplicationContext(), "一次最大插入4张图片");
					return;
				}
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				intent.putExtra("return-data", false);
				startActivityForResult(intent, 2);
			}
		});

	}

	private void showPromptSettingType() {
		AlertDialog.Builder builder = new Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		View view = LinearLayout.inflate(getApplicationContext(), R.layout.show_prompt_view, null);
		builder.setView(view);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		TextView msg = (TextView) view.findViewById(R.id.tv_msg);
		Button yes = (Button) view.findViewById(R.id.bt_yes);
		Button no = (Button) view.findViewById(R.id.bt_no);
		msg.setText("如你已对排版后的内容做出修改，那么取消排版后，修改的内容将不存在");
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				et_content.setText(tempContent);
				bt_typeSeting.setText("排版");
				dialog.dismiss();
			}
		});
		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private String			tempContent	= "";

	private ProgressDialog	progress;

	private void showCheck() {
		View v = LayoutInflater.from(this).inflate(R.layout.layout_load_pb, null);
		progress = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		progress.setCancelable(true);
		progress.setCanceledOnTouchOutside(false);
		Utils.showProgress(progress, "请稍后……", v);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_FIRST_USER) {
			final String path = UploadImageUtils.doPhoto(requestCode, data, this);
			String text = "";
			SendARCBodyImages object = new SendARCBodyImages();
			object.setKey(path);
			object.setImage(text);
			body.getImages(false).add(object);
			images.add(object);
			iAdapter.notifyDataSetChanged();
		}
	}

	private class sendAsy
			extends Asyntask2<String, String, BaseBean> {

		@Override
		protected BaseBean doInbackProgres(String... params) {
			// TODO Auto-generated method stub
			String img = "";
			UploadImageUtils uploadImageUtils = new UploadImageUtils();
			for (int i = 0; i < images.size(); i++) {
				if (i == 0) {
					body.setLitpic(images.get(i).getKey());
				}
				if (TextUtils.isEmpty(images.get(i).getImage())) {
					SendARCBodyImages image = uploadImageUtils.UploadImage(images.get(i), getApplicationContext());
					if (image != null) {
						img += image.getImage() + "<br/>";
					}
				}
				else {

					img += images.get(i).getImage() + "<br/>";

				}

			}
			content = img + content;
			content += "<p>                                                  --<a href=\"http://android.myapp.com/myapp/detail.htm?apkName=com.hxgwx.www\">来自红香阁手机客户端</a></p>";
			body.setBody(content);
			body.setTitle(title);
			body.setTypeid(typeId);
			body.setWriter(application.getUser().getData().getUname());
			body.setMid(application.getUser().getData().getMid());
			return web.publicArc(body);
		}

		@Override
		protected void doResult(BaseBean res) {
			// TODO Auto-generated method stub
			super.doResult(res);
			progress.dismiss();
			if (res != null && res.isStatu()) {
				com.hxgwx.www.utils.Utils.showLongToast(getApplicationContext(), "发表成功");
				finish();
			}
			else {
				if (res != null) {
					com.hxgwx.www.utils.Utils.showLongToast(getApplicationContext(),
							HongxianggeApplication.getInstance().getDescriptionByCode(res.getCode() + ""));
				}
				else {
					com.hxgwx.www.utils.Utils.showLongToast(getApplicationContext(), "发表失败");
				}

			}
		}

	}

	@Override
	public void add() {
		// TODO Auto-generated method stub
		ACTIVITYS.add(this);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeAll() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(getLocalClassName());
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(getLocalClassName());
		MobclickAgent.onPause(this);
	}

	public void save() {
		if (pWindow != null && pWindow.isShowing()) return;

		if (!com.hxgwx.www.utils.Utils.checkLogin(application)) {
			Utils.showLongToast(getApplicationContext(), "请登录");
			return;
		}

		title = et_title.getText().toString().trim();
		content = et_content.getText().toString().trim();
		if (TextUtils.isEmpty(title) || TextUtils.isEmpty(typeId) || TextUtils.isEmpty(content)) {
			com.hxgwx.www.utils.Utils.showLongToast(application, "数据输入不完整");
			return;
		}

		body.setBody(content);
		body.setTitle(title);
		body.setTypeid(typeId);
		body.setImages(images);
		body.setWriter(application.getUser().getData().getUname());
		body.setMid(application.getUser().getData().getMid());
		body.save(adb);
		finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (checkHasSave()) super.onBackPressed();
	}

	public boolean checkHasSave() {
		if (com.hxgwx.www.utils.Utils.checkLogin(application)) {

			title = et_title.getText().toString().trim();
			content = et_content.getText().toString().trim();
			if (TextUtils.isEmpty(title) || TextUtils.isEmpty(typeId) || TextUtils.isEmpty(content)) {
				return true;

			}
			else {
				showPromptSave();
			}
		}
		return false;
	}

	private void showPromptSave() {
		AlertDialog.Builder builder = new Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		View view = LinearLayout.inflate(getApplicationContext(), R.layout.show_prompt_view, null);
		builder.setView(view);
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		TextView msg = (TextView) view.findViewById(R.id.tv_msg);
		Button yes = (Button) view.findViewById(R.id.bt_yes);
		Button no = (Button) view.findViewById(R.id.bt_no);
		msg.setText("已检测到有内容未发表，是否保存到本地");
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				save();
			}
		});
		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		dialog.show();
	}
}
