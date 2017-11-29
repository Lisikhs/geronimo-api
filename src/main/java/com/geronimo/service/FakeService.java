package com.geronimo.service;

import com.geronimo.model.Message;
import com.geronimo.model.Profile;
import com.geronimo.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class FakeService {
    private static List<User> users = new ArrayList<>();
    private static int messageIdCount = 0;

    FakeService() {
        this.users = buildUsers();
        buildMessages();
    }

    public static List<User> getUsers() {
        return users;
    }

    private List<User> buildUsers() {
        List<User> users = new ArrayList<>();

        User user1 = buildUser(1L, "John", "Doe");
        User user2 = buildUser(2L, "Jon", "Smith");
        User user3 = buildUser(3L, "Will", "Craig");
        User user4 = buildUser(4L, "Sam", "Lernorad");
        User user5 = buildUser(5L, "Ross", "Doe");

        user3.addFollower(user1);

        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);

        return users;
    }

    private void buildMessages() {
        User JohnDoe = users.get(0);
        User WillCraig = users.get(2);

        Message message = buildMessage("This is my first message! Hello world!", JohnDoe);
        JohnDoe.addMessage(message);

        message = buildMessage("Second message is frequently (так пишется?) the most weird.", JohnDoe);
        JohnDoe.addMessage(message);
        JohnDoe.getMessages().get(0).addAnswer(message);
        message.addLike(WillCraig);
        message.addLike(users.get(1));
        message.addReblog(WillCraig);

        message = buildMessage(" Antihype.", WillCraig);
        WillCraig.addMessage(message);
        JohnDoe.getMessages().get(1).addAnswer(message);
        message.addReblog(JohnDoe);
        message.addLike(JohnDoe);
    }

    private static Message buildMessage(String text, User author) {
        Message message = new Message(text, author);
        try { //same error all the time WHAT'S WRONG WITH YOU?!
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        message.setDateCreated(LocalDateTime.now());
        message.setId((long) messageIdCount++);
        return message;
    }

    private User buildUser(Long id, String username, String password) {
        User user = new User();

        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);

        user.setProfile(new Profile());

        return user;
    }

    private Message getAnsweredMessage(Message mainMessage) {
        for (User u : users) {
            Message message = u.getMessages().stream().filter(msg -> msg.getAnswers().contains(mainMessage)).findFirst().orElse(null);
            if (message != null) return message;
        }
        return null;
    }

    private Message getMessageById(Long id) {
        for (User u : users) {
            Message message = u.getMessages().stream().filter(msg -> msg.getId().equals(id)).findFirst().orElse(null);
            if (message != null) return message;
        }
        return null;
    }

    private Long getCountOfLikes(Long id) {
        return (long) getMessageById(id).getLikes().size();
    }

    private Long getCountOfReblogs(Long id) {
        return (long) getMessageById(id).getReblogs().size();
    }

    private void addMessage(Long userId, String text) {
        User author = getUserById(userId);
        Message message = buildMessage(text, author);
        author.addMessage(message);
    }

    private List<Message> getFeed(User user) {
        List<Message> messages = new ArrayList<>();
        for (User u : users) {
            if (u.getFollowers().stream().anyMatch(user1 -> user1.getId().equals(user.getId()))) {
                messages.addAll(u.getMessages());
            }
        }

        return messages;
    }

    public User getUserById(Long id) {
        return this.users.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Message> getFeedByUserId(Long id) {
        List<Message> messages;
        User user = getUserById(id);
        if (user == null)
            return new ArrayList<>();

        messages = getFeed(user);
        messages.addAll(user.getMessages());

        messages.sort((o1, o2) -> o2.getDateCreated().compareTo(o1.getDateCreated()));
        return messages;
    }

    public Boolean saveUser(User user) {
        if (users.stream().anyMatch(u -> u.getUsername().equals(user.getUsername()))) {
            return false;
        }

        Long nextId = users.get(users.size()-1).getId() + 1;
        user.setId(nextId);
        users.add(user);

        return true;
    }

    public User validateUser(User user) {
        Optional<User> optional = users.stream().filter(u -> u.getUsername().equals(user.getUsername()) &&
                u.getPassword().equals(user.getPassword())).findFirst();
        return optional.orElse(null);
    }

    public User updateUser(User user) {
        User modifiedUser = users.stream().filter(u -> u.getId().equals(user.getId())).findFirst().orElse(null);

        if (modifiedUser != null) {
            modifiedUser.setUsername(user.getUsername());
            modifiedUser.setPassword(user.getPassword());
            modifiedUser.setProfile(user.getProfile());
        } else {
            throw new RuntimeException("User with id = " + user.getId() + " not found");
        }

        return modifiedUser;
    }

    public Boolean deleteUserById(Long id) {
        User deleteUser = users.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);

        if (deleteUser != null) {
            users.remove(deleteUser);
            return true;
        }

        return false;
    }

    public List<Message> getMessagesByMessageId(Long id) {
        Message message = getMessageById(id);
        if (message == null) {
            return null;
        }

        List<Message> messages = new ArrayList<>();
        messages.add(message);

        if (!message.getAnswers().isEmpty()) {
            messages.add(message.getAnswers().get(0));
        }

        Message answeredMessage = getAnsweredMessage(message);
        if (answeredMessage != null) {
            messages.add(answeredMessage);
        }

        messages.sort(Comparator.comparing(o -> o.getDateCreated()));
        return messages;
    }

    public Long getCountOfLikesByMessageId(Long id) {
        return getCountOfLikes(id);
    }

    public Long getCountOfReblogsByMessageId(Long id) {
        return getCountOfReblogs(id);
    }

    public Message answerMessage(Long answeredId, Message message) {
        Message finalMessage = message;
        User author = users.stream().filter(u -> u.getUsername().equals(finalMessage.getAuthor().getUsername())).findFirst().orElse(null);
        message = buildMessage(message.getText(), author);

        author.addMessage(message);
        Message answeredMessage = getMessageById(answeredId);
        answeredMessage.addAnswer(message);

        return message;
    }

    public void postMessage(Message message) {
        addMessage(message.getAuthor().getId(), message.getText());
    }

    public void likeMessage(Long idOfMessage, String idOfUser) {
        User userWhoLiked = users.stream()
                .filter(u -> u.getId().equals(Long.parseLong(idOfUser)))
                .findFirst().orElse(null);
        Message message = getMessageById(idOfMessage);

        message.addLike(userWhoLiked);
    }

    public void dislikeMessage(Long idOfMessage, String idOfUser) {
        User userWhoDisliked = users.stream()
                .filter(u -> u.getId().equals(Long.parseLong(idOfUser)))
                .findFirst().orElse(null);
        Message message = getMessageById(idOfMessage);

        message.removeLike(userWhoDisliked);
    }

    public boolean isLiked(Long idOfMessage, Long idOfUser) {
        Message message = getMessageById(idOfMessage);
        return message.getLikes().stream().anyMatch(u -> u.getId().equals(idOfUser));
    }

    public boolean isReblogged(Long idOfMessage, Long idOfUser) {
        Message message = getMessageById(idOfMessage);
        return message.getReblogs().stream().anyMatch(u -> u.getId().equals(idOfUser));
    }

    public void reblog(Long idOfMessage, String idOfUser) {
        User userWhoReblogged = users.stream()
                .filter(u -> u.getId().equals(Long.parseLong(idOfUser)))
                .findFirst().orElse(null);
        Message message = getMessageById(idOfMessage);

        message.addReblog(userWhoReblogged);
    }

    public void removeReblog(Long idOfMessage, String idOfUser) {
        User userWhoDisliked = users.stream()
                .filter(u -> u.getId().equals(Long.parseLong(idOfUser)))
                .findFirst().orElse(null);
        Message message = getMessageById(idOfMessage);

        message.removeReblog(userWhoDisliked);
    }

    public List<Message> getMessagesByUserId(Long id) {
        return getUserById(id).getMessages();
    }

    public Boolean isSubscribedTo(Long idOfSessionUser, Long idOfUser) {
        Set<User> users = getUserById(idOfUser).getFollowers();
        for (User u: users) {
            if (u.getId().equals(idOfSessionUser))
                return true;
        }
        return false;
    }

    public void subscribeTo(Long idOfSessionUser, Long idOfUser) {
        getUserById(idOfUser).addFollower(getUserById(idOfSessionUser));
    }

    public void unsubscribeFrom(Long idOfSessionUser, Long idOfUser) {
        getUserById(idOfUser).removeFollower(getUserById(idOfSessionUser));
    }

    public int[] getUserInfo(Long idOfUser) {
        User user = getUserById(idOfUser);
        int[] infoPieces = new int[3];
        infoPieces[0] = user.getMessages().size();
        infoPieces[1] = getFollowersOfUser(user);
        infoPieces[2] = user.getFollowers().size();
        return infoPieces;
    }

    private int getFollowersOfUser(User user) {
        int count = 0;
        for (User u: users) {
            if (u.getFollowers().contains(user)) {
                count++;
            }
        }
        return count;
    }
}