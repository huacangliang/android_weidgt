package com.hxgwx.www.fragment;

import java.io.File;
import java.io.InputStream;

import com.hongxiangge.image.ImageManager2;
import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.R;
import com.hxgwx.www.activity.AboutActivity;
import com.hxgwx.www.activity.BookMarkActivity;
import com.hxgwx.www.activity.ChoiceImageActivity;
import com.hxgwx.www.activity.LoginActivity;
import com.hxgwx.www.activity.MainFragmentActivity;
import com.hxgwx.www.activity.MyPublicActivity;
import com.hxgwx.www.activity.RegisterActivity;
import com.hxgwx.www.bean.ImageUploadBean;
import com.hxgwx.www.bean.UserLogin;
import com.hxgwx.www.utils.UploadImageUtils;
import com.hxgwx.www.view.SlidingMenu.OnOpenedListener;
import com.hxgwx.www.web.WebPlus;
import com.hxgwx.www.webimpl.WebPlusImpl;
import com.jingzhong.asyntask2.annotation.CreateView;
import com.jingzhong.asyntask2.utils.HttpUtils.OnNetListenear;
import com.jingzhong.asyntask2.utils.HttpUtils.Result;
import com.jingzhong.asyntask2.utils.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UserFragment
		extends Fragment
		implements OnOpenedListener {

	@CreateView("tv_about")
	private TextView				tv_about;

	@CreateView("tv_uname")
	private TextView				tv_uname;

	@CreateView("tv_user_score")
	private TextView				tv_user_score;

	@CreateView("tv_user_name")
	private TextView				tv_user_name;

	@CreateView("ic_user_icon")
	private ImageView				ic_user_icon;

	@CreateView("bt_logout")
	private Button					bt_logout;

	@CreateView("bt_bm")
	private Button					bt_bm;

	@CreateView("bt_mp")
	private Button					bt_mp;

	@CreateView("pb_upload_face")
	private ProgressBar				pb_upload_face;

	@CreateView("tv_p_")
	private TextView				tv_p_;

	private HongxianggeApplication	application;

	/**
	 * @return the application
	 */
	public HongxianggeApplication getApplication2() {
		return application;
	}

	/**
	 * @param application
	 *            the application to set
	 */
	public void setApplication(HongxianggeApplication application) {
		this.application = application;
	}

	/**
	 * @return the tv_about
	 */
	public TextView getTv_about() {
		return tv_about;
	}

	/**
	 * @param tv_about
	 *            the tv_about to set
	 */
	public void setTv_about(TextView tv_about) {
		this.tv_about = tv_about;
	}

	/**
	 * @return the tv_uname
	 */
	public TextView getTv_uname() {
		return tv_uname;
	}

	/**
	 * @param tv_uname
	 *            the tv_uname to set
	 */
	public void setTv_uname(TextView tv_uname) {
		this.tv_uname = tv_uname;
	}

	/**
	 * @return the tv_user_score
	 */
	public TextView getTv_user_score() {
		return tv_user_score;
	}

	/**
	 * @param tv_user_score
	 *            the tv_user_score to set
	 */
	public void setTv_user_score(TextView tv_user_score) {
		this.tv_user_score = tv_user_score;
	}

	/**
	 * @return the tv_user_name
	 */
	public TextView getTv_user_name() {
		return tv_user_name;
	}

	/**
	 * @param tv_user_name
	 *            the tv_user_name to set
	 */
	public void setTv_user_name(TextView tv_user_name) {
		this.tv_user_name = tv_user_name;
	}

	/**
	 * @return the ic_user_icon
	 */
	public ImageView getIc_user_icon() {
		return ic_user_icon;
	}

	/**
	 * @param ic_user_icon
	 *            the ic_user_icon to set
	 */
	public void setIc_user_icon(ImageView ic_user_icon) {
		this.ic_user_icon = ic_user_icon;
	}

	/**
	 * @return the ll_user_unlogin
	 */
	public LinearLayout getLl_user_unlogin() {
		return ll_user_unlogin;
	}

	/**
	 * @param ll_user_unlogin
	 *            the ll_user_unlogin to set
	 */
	public void setLl_user_unlogin(LinearLayout ll_user_unlogin) {
		this.ll_user_unlogin = ll_user_unlogin;
	}

	/**
	 * @return the ll_user_login
	 */
	public LinearLayout getLl_user_login() {
		return ll_user_login;
	}

	/**
	 * @param ll_user_login
	 *            the ll_user_login to set
	 */
	public void setLl_user_login(LinearLayout ll_user_login) {
		this.ll_user_login = ll_user_login;
	}

	/**
	 * @return the bt_login
	 */
	public Button getBt_login() {
		return bt_login;
	}

	/**
	 * @param bt_login
	 *            the bt_login to set
	 */
	public void setBt_login(Button bt_login) {
		this.bt_login = bt_login;
	}

	/**
	 * @return the bt_register
	 */
	public Button getBt_register() {
		return bt_register;
	}

	/**
	 * @param bt_register
	 *            the bt_register to set
	 */
	public void setBt_register(Button bt_register) {
		this.bt_register = bt_register;
	}

	/**
	 * @return the user
	 */
	public UserLogin getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(UserLogin user) {
		this.user = user;
	}

	@CreateView("ll_user_unlogin")
	private LinearLayout	ll_user_unlogin;

	@CreateView("ll_user_login")
	private LinearLayout	ll_user_login;

	@CreateView("bt_login")
	private Button			bt_login;

	@CreateView("bt_register")
	private Button			bt_register;

	@CreateView("tv_top_about")
	private TextView		tv_top_about;

	MainFragmentActivity	activity;

	private UserLogin		user;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		activity = (MainFragmentActivity) getActivity();
		View v = inflater.inflate(R.layout.user_center, null);
		try {
			Utils.injectObject(this, v, R.id.class);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		application = (HongxianggeApplication) getActivity().getApplication();
		activity.getMenu().setOnOpenedListener(this);
		initView(getView());
		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (!hidden) {
			// initView(getView());
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	private void initView(View v) {

		tv_about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				com.hxgwx.www.utils.Utils.gotoActivity(getActivity(), AboutActivity.class, null);
			}
		});

		bt_bm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(activity, BookMarkActivity.class));
			}
		});

		bt_mp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(activity, MyPublicActivity.class));
			}
		});

	}

	@SuppressWarnings({})
	private void initLogin() {
		user = activity.getApplication2().getUser();
		if (user != null && user.isStatu()) {
			ll_user_login.setVisibility(View.VISIBLE);
			ll_user_unlogin.setVisibility(View.GONE);
			tv_top_about.setVisibility(View.GONE);
			tv_uname.setText(user.getData().getUname());
			tv_user_name.setText(user.getData().getUserid());
			tv_user_score.setText(user.getData().getScores());
			ic_user_icon.setTag("");
			ic_user_icon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), ChoiceImageActivity.class);
					startActivityForResult(intent, 1);
					getActivity().overridePendingTransition(R.anim.an_pow_out, R.anim.an_pow_in);
				}
			});
			bt_logout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					application.setUser(null);
					user = null;
					initLogin();
				}
			});
			ImageManager2.from(activity, true).displayImage(ic_user_icon,
					HongxianggeApplication.getInstance().getDomain() + user.getData().getFace(),
					R.drawable.username_edit_icon);

		}
		else {
			ll_user_login.setVisibility(View.GONE);
			ll_user_unlogin.setVisibility(View.VISIBLE);
			tv_top_about.setVisibility(View.VISIBLE);
			tv_top_about.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					com.hxgwx.www.utils.Utils.gotoActivity(getActivity(), AboutActivity.class, null);
				}
			});
			bt_login.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.putExtra("from", 1);
					// TODO Auto-generated method stub
					com.hxgwx.www.utils.Utils.gotoActivity(activity, LoginActivity.class, i);
				}
			});
			bt_register.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent();
					i.putExtra("from", 2);
					// TODO Auto-generated method stub
					com.hxgwx.www.utils.Utils.gotoActivity(activity, RegisterActivity.class, i);
				}
			});
		}
	}

	@Override
	public void onOpened() {
		initLogin();
	}

	private String path;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			path = data.getStringExtra(ChoiceImageActivity.IMAGEPATH);
			ImageManager2.from(activity, true).displayImage(ic_user_icon, path, R.drawable.username_edit_icon);
			uploadFace();
		}
	}

	Handler	pHandler	= new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				int p = msg.arg1;
				pb_upload_face.setProgress(p);
				tv_p_.setVisibility(View.VISIBLE);
				tv_p_.setText(p + "%");
			}
			if (msg.what == 2) {
				pb_upload_face.setVisibility(View.GONE);
				tv_p_.setVisibility(View.VISIBLE);
				tv_p_.setText("上传头像失败");
			}
			if (msg.what == 3) {
				ImageManager2.from(activity, true).displayImage(ic_user_icon,
						HongxianggeApplication.getInstance().getDomain() + user.getData().getFace(),
						R.drawable.username_edit_icon);
				pb_upload_face.setVisibility(View.GONE);
				tv_p_.setVisibility(View.GONE);
				Utils.showShortToast(getActivity(), "上传头像成功");
			}
		};
	};

	String	tPath;

	private void uploadFace() {
		pb_upload_face.setVisibility(View.VISIBLE);
		tv_p_.setVisibility(View.GONE);
		WebPlus web = new WebPlusImpl(getActivity());
		File root = new File(path);
		tPath = null;
		tPath = com.hxgwx.www.utils.Utils.getExtenalPath(application,
				application.getPackageName() + "/data/cache/image/");

		Bitmap bit = UploadImageUtils.getBitmapFromPath(path, 180, 180);
		bit = UploadImageUtils.compressImage(bit, 50);
		InputStream is = com.jingzhong.asyntask2.utils.Utils.Bitmap2IS(bit);
		try {
			tPath = com.jingzhong.asyntask2.utils.Utils.save(is, tPath, root.getName());
		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (tPath == null) {
			pHandler.sendMessage(pHandler.obtainMessage(2, 0, 0));
			return;
		}
		web.editFace(user.getData().getMid(), tPath, user.getData().getFace(), new OnNetListenear() {

			@Override
			public void onUploadListener(long progress, long tatol) {
				// TODO Auto-generated method stub
				int p = (int) ((progress / tatol) * 100);
				if (p == 100) {
					p = 99;
				}
				pHandler.sendMessage(pHandler.obtainMessage(1, p, 0));
			}

			@Override
			public void onSuccess(Result r) {
				// TODO Auto-generated method stub
				new File(tPath).delete();
				String res = r.string();
				if (res != null) {
					ImageUploadBean bean = (ImageUploadBean) com.hxgwx.www.utils.Utils
							.jsonPasreToObject(ImageUploadBean.class, res);
					if (bean != null && bean.isStatu() && bean.getCode() == 0) {
						user.getData().setFace(bean.getPath());
						pHandler.sendMessage(pHandler.obtainMessage(3, 0, 0));
						return;
					}
				}
				pHandler.sendMessage(pHandler.obtainMessage(2, 0, 0));
			}

			@Override
			public void onError(Exception e, int status) {
				// TODO Auto-generated method stub
				new File(tPath).delete();
				pHandler.sendMessage(pHandler.obtainMessage(2, 0, 0));
			}

			@Override
			public void onDwonloadListener(long progress, long tatol) {
				// TODO Auto-generated method stub

			}
		});
	}

}
