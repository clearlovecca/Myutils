package com.bawei.shutype.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.bawei.shutype.dao.Food;
import com.bawei.shutype.dao.Shop;

import com.bawei.shutype.dao.FoodDao;
import com.bawei.shutype.dao.ShopDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig foodDaoConfig;
    private final DaoConfig shopDaoConfig;

    private final FoodDao foodDao;
    private final ShopDao shopDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        foodDaoConfig = daoConfigMap.get(FoodDao.class).clone();
        foodDaoConfig.initIdentityScope(type);

        shopDaoConfig = daoConfigMap.get(ShopDao.class).clone();
        shopDaoConfig.initIdentityScope(type);

        foodDao = new FoodDao(foodDaoConfig, this);
        shopDao = new ShopDao(shopDaoConfig, this);

        registerDao(Food.class, foodDao);
        registerDao(Shop.class, shopDao);
    }
    
    public void clear() {
        foodDaoConfig.clearIdentityScope();
        shopDaoConfig.clearIdentityScope();
    }

    public FoodDao getFoodDao() {
        return foodDao;
    }

    public ShopDao getShopDao() {
        return shopDao;
    }

}
