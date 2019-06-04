package com.borun.easybill.mvp.model.Imp;

import com.borun.easybill.base.BaseObserver;
import com.borun.easybill.model.bean.BaseBean;
import com.borun.easybill.model.bean.local.BBill;
import com.borun.easybill.model.bean.local.BPay;
import com.borun.easybill.model.bean.local.BSort;
import com.borun.easybill.model.bean.local.NoteBean;
import com.borun.easybill.model.repository.LocalRepository;
import com.borun.easybill.mvp.model.BillModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BillModelImp implements BillModel {

    private BillOnListener listener;

    public BillModelImp(BillOnListener listener) {
        this.listener = listener;
    }

    @Override
    public void getNote() {
        final NoteBean note = new NoteBean();
        LocalRepository.getInstance()
                .getBPay()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<BPay>>() {
                    @Override
                    protected void onSuccess(List<BPay> bPays) throws Exception {
                        note.setPayinfo(bPays);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
        LocalRepository.getInstance().getBSort(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<BSort>>() {
                    @Override
                    protected void onSuccess(List<BSort> sorts) throws Exception {
                        note.setOutSortlis(sorts);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
        LocalRepository.getInstance().getBSort(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<BSort>>() {
                    @Override
                    protected void onSuccess(List<BSort> sorts) throws Exception {
                        note.setInSortlis(sorts);
                        listener.onSuccess(note);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
    }

    @Override
    public void add(BBill bBill) {
        LocalRepository.getInstance().saveBBill(bBill)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BBill>() {
                    @Override
                    protected void onSuccess(BBill bBill) throws Exception {
                        listener.onSuccess(new BaseBean());
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
    }

    @Override
    public void update(BBill bBill) {
        LocalRepository.getInstance()
                .updateBBill(bBill)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BBill>() {
                    @Override
                    protected void onSuccess(BBill bBill) throws Exception {
                        listener.onSuccess(new BaseBean());
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
    }

    @Override
    public void delete(String id) {

        LocalRepository.getInstance()
                .deleteBBillById(id);
    }

    @Override
    public void onUnsubscribe() {

    }

    /**
     * 回调接口
     */
    public interface BillOnListener {
        void onSuccess(BaseBean bean);

        void onSuccess(NoteBean bean);

        void onFailure(Throwable e);
    }
}
