package com.bawei.shutype.mvp.presenter;


import com.bawei.shutype.mvp.model.IModel;
import com.bawei.shutype.mvp.view.IView;

public class BasePresnter<M extends IModel,V extends IView> implements IPresenter{
    protected M mModel;
    protected V mView;

    public BasePresnter(M mModel, V mView) {
        this.mModel = mModel;
        this.mView = mView;
    }
}
