package com.techcourse.service;

import com.techcourse.dao.UserDao;
import com.techcourse.dao.UserHistoryDao;
import com.techcourse.domain.User;
import com.techcourse.domain.UserHistory;
import org.springframework.transaction.support.TransactionTemplate;

public class UserService {
    private final TransactionTemplate transactionTemplate;
    private final UserDao userDao;
    private final UserHistoryDao userHistoryDao;

    public UserService(final TransactionTemplate transactionTemplate,
                       final UserDao userDao,
                       final UserHistoryDao userHistoryDao) {
        this.transactionTemplate = transactionTemplate;
        this.userDao = userDao;
        this.userHistoryDao = userHistoryDao;
    }

    public User findById(final long id) {
        return userDao.findById(id);
    }

    public void insert(final User user) {
        userDao.insert(user);
    }

    public void changePassword(final long id, final String newPassword, final String createBy) {
        transactionTemplate.execute(() -> {
            final var user = findById(id);
            user.changePassword(newPassword);
            userDao.updatePassword(user);
            userHistoryDao.log(new UserHistory(user, createBy));
            return null;
        });
    }
}
