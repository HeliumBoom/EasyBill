package com.borun.easybill.mvp.model.Imp;

import com.borun.easybill.base.BaseObserver;
import com.borun.easybill.model.bean.local.BBill;
import com.borun.easybill.model.bean.local.MonthAccountBean;
import com.borun.easybill.model.repository.LocalRepository;
import com.borun.easybill.mvp.model.MonthAccountModel;
import com.borun.easybill.utils.BillUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class MonthAccountModelImp implements MonthAccountModel {

    private MonthAccountOnListener listener;

    public MonthAccountModelImp(MonthAccountOnListener listener) {
        this.listener = listener;
    }


    @Override
    public void getMonthAccountBills(String id, String year, String month) {
        LocalRepository.getInstance().getBBillByUserIdWithYM(id, year, month)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<BBill>>() {
                    @Override
                    protected void onSuccess(List<BBill> bBills) throws Exception {
                        listener.onSuccess(BillUtils.packageAccountList(bBills));
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
    public interface MonthAccountOnListener {

        void onSuccess(MonthAccountBean bean);

        void onFailure(Throwable e);
    }
}
