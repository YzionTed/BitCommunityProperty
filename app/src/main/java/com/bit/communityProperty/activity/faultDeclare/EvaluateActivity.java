package com.bit.communityProperty.activity.faultDeclare;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.utils.GsonUtils;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.utils.ToastUtil;
import com.bit.communityProperty.view.StarsView.StarsAdapter;
import com.bit.communityProperty.view.StarsView.StarsView;
import com.bit.communityProperty.view.TitleBarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * 评价申报单页面
 * Created by kezhangzhao on 2018/3/13.
 */

public class EvaluateActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏
    private StarsView mStarsView;//五星评论自定义view
    private EditText etEvaluate;//评论内容
    private Button btConfirm;//确认评价
    private int starsNum = 1;//评价等级，星星数量
    private String faultID;//故障单号
    private PromptDialog mPromptDialog;//弹窗

    @Override
    public int getLayoutId() {
        return R.layout.activity_evaluate;
    }

    @Override
    public void initViewData() {
        faultID = getIntent().getStringExtra("FaultID");
        mPromptDialog = new PromptDialog((Activity) mContext);
        mTitleBarView = findViewById(R.id.titlebarview);
        mTitleBarView.setTvTitleText("维修评价");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btConfirm = findViewById(R.id.bt_confirm);
        etEvaluate = findViewById(R.id.et_evaluate);
        mStarsView = findViewById(R.id.view_stars);
        mStarsView.setmStarsNum(starsNum, 5);//设置黄色星星和灰色星星数量
        mStarsView.setPadding(3, 3, 3, 3);//设置单个星星的padding
        mStarsView.setOnMyItemClickListener(new StarsAdapter.OnMyItemClickListener() {
            @Override
            public void myClick(View view, int position) {
                mStarsView.setmStarsNum(position + 1, 5);//设置黄色星星和灰色星星数量
                starsNum = position + 1;
            }
        });
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(etEvaluate.getText())) {
                    mPromptDialog.showLoading("正在上传评论内容...");
                    evaluateFault(faultID, starsNum, etEvaluate.getText().toString(), null);
                } else {
                    ToastUtil.showTextShort(mContext, "评论内容不能为空");
                }
            }
        });
    }

    /**
     * 住户评价故障申报单
     *
     * @param faultID             故障单号
     * @param evaluationGrade     评价等级
     * @param evaluation          评论内容
     * @param evaluationAccessory 图片列表 （非必填）
     */
    private void evaluateFault(final String faultID, int evaluationGrade, String evaluation,
                               ArrayList<String> evaluationAccessory) {
        Map<String, Object> map = new HashMap<>();
        map.put("faultID", faultID);
        map.put("evaluationGrade", evaluationGrade);
        map.put("evaluation", evaluation);
        if (evaluationAccessory != null)
            map.put("evaluationAccessory", evaluationAccessory);
        RetrofitManage.getInstance().subscribe(Api.getInstance().evaluateFault(map), new Observer<BaseEntity<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<String> baseEntity) {
                LogManager.printErrorLog("backinfo", GsonUtils.getInstance().toJson(baseEntity));
                if (baseEntity.isSuccess()) {
                    mPromptDialog.showLoading("评论完成");
                    Intent intent = new Intent();
                    intent.putExtra("FaultID", faultID);
                    EvaluateActivity.this.setResult(RESULT_OK, intent);
                    finish();
                } else {
                    mPromptDialog.showError(baseEntity.getErrorMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                mPromptDialog.showError("评论失败");
            }

            @Override
            public void onComplete() {
            }
        });
    }

}
