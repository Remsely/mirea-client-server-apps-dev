package edu.mirea.remsely.csad.semester7.practice3.task3;


import io.reactivex.rxjava3.core.Observable;

import java.util.*;
import java.util.stream.IntStream;

public class UserFriendService {
    public static void main(String[] args) {
        Random random = new Random();

        List<UserFriend> db = createSampleUsersDatabase(10, 50);
        System.out.println("Sample DB:");
        db.stream()
                .sorted(Comparator.comparing(UserFriend::userId))
                .forEach(System.out::println);
        System.out.println();

        List<Integer> queryUserIds = IntStream.range(0, 3)
                .map(i -> 1 + random.nextInt(10))
                .boxed()
                .toList();
        System.out.println("Query userIds: " + queryUserIds);
        System.out.println();

        System.out.println("Friends (flatMap of getFriends):");
        Observable.fromIterable(queryUserIds)
                .flatMap(id -> getFriends(db, id))
                .blockingForEach(System.out::println);
    }

    private static Observable<UserFriend> getFriends(List<UserFriend> database, int userId) {
        return Observable.fromIterable(database).filter(uf -> uf.userId() == userId);
    }

    private static List<UserFriend> createSampleUsersDatabase(int totalUsers, int relations) {
        Random random = new Random();
        Set<String> seen = new HashSet<>();
        List<UserFriend> db = new ArrayList<>(relations);
        while (db.size() < relations) {
            int u = 1 + random.nextInt(totalUsers);
            int f = 1 + random.nextInt(totalUsers);
            if (u == f) continue;
            String key = u + "-" + f;
            if (seen.add(key)) {
                db.add(new UserFriend(u, f));
            }
        }
        return db;
    }

    private record UserFriend(int userId, int friendId) {
    }
}
