package com.borun.easybill.mvp.model.Imp;

import com.borun.easybill.api.RetrofitFactory;
import com.borun.easybill.base.BaseObserver;
import com.borun.easybill.model.bean.remote.UserBean;
import com.borun.easybill.mvp.model.UserLogModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserLogModelImp implements UserLogModel {

    private UserLogOnListener listener;

    public UserLogModelImp(UserLogOnListener listener) {
        this.listener = listener;
    }

    @Override
    public void login(String username, String password) {

//        Map<String,String> params = new HashMap<String,String>();
//        params.put("username",username);
//        params.put("password",password);
//        OkHttpUtils.getInstance().post(Constants.BASE_URL + Constants.USER_LOGIN, params, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                listener.onFailure(e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("TAG", "onResponse: "+response.body().string());
//                MyUser myUser = GsonUtils.fromJson(response.body().string(),MyUser.class);
//
//                //listener.onSuccess(myUser);
//            }
//        });

        RetrofitFactory.getInstence().API()
                .login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<UserBean>() {
                    @Override
                    protected void onSuccees(UserBean userBean) throws Exception {
                        listener.onSuccess(userBean);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
    }

    @Override
    public void signup(String username, String password, String mail) {
       RetrofitFactory.getInstence().API()
                .signup(username, password, mail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<UserBean>() {
                    @Override
                    protected void onSuccees(UserBean userBean) throws Exception {
                        listener.onSuccess(userBean);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
    }

    @Override
    public void onUnsubscribe() {

    }

    /**
     * 回调接口
     */
    public interface UserLogOnListener {

        void onSuccess(UserBean user);

        void onFailure(Throwable e);
    }
}
