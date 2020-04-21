package tests.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;

import com.jonrib.app.WebApplication;
import com.jonrib.app.model.Comment;
import com.jonrib.app.repository.CommentRepository;
import com.jonrib.app.service.CommentService;
import com.jonrib.app.service.CommentServiceImpl;

public class CommentServiceTests {
	private final CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
	
	private CommentService commentService;
	
	@BeforeEach
	void initUseCase() {
		commentService = new CommentServiceImpl(commentRepository);
	}
	
	@Test
	void saveComment() {
		Comment comment = new Comment();
		comment.setDate(new Date());
		comment.setId(Long.parseLong("1"));
		comment.setMessage("");
		comment.setReplies(null);
		comment.setUserName("");
		Mockito.when(commentRepository.save(comment)).then(new Answer<Comment>() {
			@Override
			public Comment answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return (Comment) args[0];
			}
		});
		Comment savedComment = (Comment) commentService.save(comment);
		assertEquals(savedComment, comment);
	}
	
	@Test
	void deleteComment() {
		Comment comment = new Comment();
		comment.setDate(new Date());
		comment.setId(Long.parseLong("1"));
		comment.setMessage("");
		comment.setReplies(null);
		comment.setUserName("");
		commentService.delete(comment);
	}
	
	@Test
	void findAll() {
		Comment comment = new Comment();
		comment.setDate(new Date());
		comment.setId(Long.parseLong("1"));
		comment.setMessage("");
		comment.setReplies(null);
		comment.setUserName("");
		Mockito.when(commentRepository.findAll()).then(new Answer<List<Comment>>() {
			@Override
			public List<Comment> answer(InvocationOnMock invocation) throws Throwable {
				List<Comment> list = new ArrayList<Comment>();
				list.add(comment);
				return list;
			}
		});
		Comment savedComment = commentService.findAll().get(0);
		assertEquals(savedComment, comment);
	}
	
	@Test
	void findById() {
		Comment comment = new Comment();
		comment.setDate(new Date());
		comment.setId(Long.parseLong("1"));
		comment.setMessage("");
		comment.setReplies(null);
		comment.setUserName("");
		Mockito.when(commentRepository.findById(Long.parseLong("1"))).then(new Answer<Optional<Comment>>() {
			@Override
			public Optional<Comment> answer(InvocationOnMock invocation) throws Throwable {
				return Optional.of(comment);
			}
		});
		Comment savedComment = commentService.findById((Long.parseLong("1"))).get();
		assertEquals(savedComment, comment);
	}
}
