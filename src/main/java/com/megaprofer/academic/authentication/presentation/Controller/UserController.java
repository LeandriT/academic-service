package com.megaprofer.academic.authentication.presentation.Controller;

import com.megaprofer.academic.authentication.entity.User;
import com.megaprofer.academic.authentication.presentation.UserPresenter;
import com.megaprofer.academic.authentication.service.UserService;
import com.megaprofer.academic.authentication.presentation.Paginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getUser")
    @ResponseBody
    public UserPresenter getCurrentUser() {
        UserPresenter userPresenter = new UserPresenter();
        User user = userService.findCurrentUser();
        userPresenter.setDni(user.getDni());
        userPresenter.setReferenceId(user.getReferenceId());
        userPresenter.setPassword("");
        userPresenter.setUserName(user.getUsername());
        userPresenter.setFullName(user.getFullName());
        userPresenter.setUserId(user.getUserId());
        return userPresenter;
    }

    @RequestMapping(value = "/usersByCriterion")
    @ResponseBody
    public Paginator getUsersByCriterion (@RequestParam("searchValue") String searchValue,
                                          @RequestParam("page") Integer page,
                                          @RequestParam("size") Integer size) {

        Paginator users = userService.getPaginatedUsers(searchValue, page, size);
        List<UserPresenter> userPresenters = new ArrayList<>();
        users.getData().forEach(object -> {
            User user = (User) object;
            UserPresenter userPresenter = UserPresenter.builder()
                    .userId(user.getUserId())
                    .userName(user.getUsername())
                    .fullName(user.getFullName())
                    .dni(user.getDni())
                    .build();
            userPresenters.add(userPresenter);

        });
        Paginator paginatorUserPresenter = new Paginator(users.getTotalPages(), users.getTotalElements(), new TreeSet(userPresenters));
        return paginatorUserPresenter;
    }
}
