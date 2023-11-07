package com.zosh.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zosh.exception.ComException;
import com.zosh.exception.ListException;
import com.zosh.exception.TwitException;
import com.zosh.exception.UserException;
import com.zosh.model.Twit;
import com.zosh.model.User;
import com.zosh.repository.ComRepository;
import com.zosh.repository.ListRepository;
import com.zosh.repository.TwitRepository;
import com.zosh.request.TwitReplyRequest;

@Service
public class TwitServiceImplementation implements TwitService {

	private TwitRepository twitRepository;
	private ListRepository listRepository;

	public TwitServiceImplementation(TwitRepository twitRepository, ListRepository listRepository) {
		this.twitRepository = twitRepository;
		this.listRepository = listRepository;
	}

	@Override
	public Twit createTwit(Twit req, User user) {

		Twit twit = new Twit();
		twit.setContent(req.getContent());
		twit.setCreatedAt(LocalDateTime.now());
		twit.setRetwitAt(LocalDateTime.now());
		twit.setImage(req.getImage());
		twit.setUser(user);
		twit.setReply(false);
		twit.setTwit(true);
		twit.setVideo(req.getVideo());

		return twitRepository.save(twit);
	}
	
	@Override
	public Twit inputethic(Long twitId, String ethicrate) throws TwitException {
		
		Twit twit = findById(twitId);
		twit.setEthicrate(ethicrate);
		//윤리수치를 가지고 온다.
		
		JsonObject object = JsonParser.parseString(ethicrate).getAsJsonObject();
		JsonArray array = object.getAsJsonArray("result");
		JsonArray values =array.get(0).getAsJsonArray();
		//json타입에 배열로 가지고 와서 해당 배열을 읽어 values에 넣는다.
		
		int maxindex = 0;
		double maxvalue = values.get(0).getAsDouble();
		
		for (int i = 1; i < values.size(); i++) {
			double value = values.get(i).getAsDouble();
			if(value > maxvalue) {
				maxvalue = value;
				maxindex = i;
			}
		}
		twit.setLabel(maxindex);
		//수치 중 가장 큰 value의 인덱스를 maxindex에 넣는다.
		
		String sentence="";
		if (maxindex == 0) {
			sentence="과격한 감정이 느껴지네여.";
		} else if (maxindex == 1) {
			sentence="흠, 성적인 감정이 느껴지는것 같네여.";
		} else if (maxindex == 2) {
			sentence="진정하세여! 욕은 안되여!!";
		} else if (maxindex == 3) {
			sentence="차별이 느껴지네여, 좀 더 평등하게 생각해봐여!";
		} else if (maxindex == 4) {
			sentence="오늘 당신의 감정은 평온 하군여.";
		}
		//maxindex에 해당하는 문장을 선택한다.
		//0: '폭력',1: '선정',2: '욕설',3: '차별',4: '정상'
		
		twit.setSentence(sentence);
		//해당 문장을 입력한다.
		
		return twitRepository.save(twit);
	}

	@Override
	public Twit retwit(Long twitId, User user) throws TwitException {
		Twit twit = findById(twitId);
		if (twit.getRetwitUser().contains(user)) {
			twit.getRetwitUser().remove(user);
		} else {
			twit.getRetwitUser().add(user);
		}
		twit.setRetwitAt(LocalDateTime.now());
		return twitRepository.save(twit);
	}

	@Override
	public Twit findById(Long twitId) throws TwitException {

		Twit twit = twitRepository.findById(twitId)
				.orElseThrow(() -> new TwitException("Twit Not Found With Id " + twitId));

		return twit;
	}

	@Override
	public void deleteTwitById(Long twitId, Long userId) throws TwitException, UserException {
		Twit twit = findById(twitId);

		if (!userId.equals(twit.getUser().getId())) {
			throw new UserException("you can't delete another users twit");
		}
		twitRepository.deleteById(twit.getId());

	}

	@Override
	public Twit removeFromRetwit(Long twitId, User user) throws TwitException, UserException {

		Twit twit = findById(twitId);

		twit.getRetwitUser().remove(user);

		return twitRepository.save(twit);
	}

	@Override
	public Twit createReply(TwitReplyRequest req, User user) throws TwitException {
		// TODO Auto-generated method stub

		Twit twit = findById(req.getTwitId());

		Twit reply = new Twit();
		reply.setContent(req.getContent());
		reply.setCreatedAt(LocalDateTime.now());
		reply.setImage(req.getImage());
		reply.setUser(user);
		reply.setReplyFor(twit);
		reply.setReply(true);
		reply.setTwit(false);

		Twit savedReply = twitRepository.save(reply);

		twit.getReplyTwits().add(savedReply);
		twitRepository.save(twit);
		return twit;
	}
	
	@Override
	public Twit editTwit(Twit req, User user) throws TwitException {
		Twit twit = findById(req.getId());
		
		twit.setContent(req.getContent());
		twit.setImage(req.getImage());
		twit.setVideo(req.getVideo());
		twit.setEdited(req.isEdited());
		twit.setEditedAt(req.getEditedAt());
		System.out.println(twit.isEdited());
		System.out.println(twit.getEditedAt());
		twitRepository.save(twit);
		return twit;
	}

	@Override
	public void deleteReply(Long replyId, Long userId) throws TwitException, UserException {
		// 특정 답글(Twit)을 찾습니다.
		Twit reply = findById(replyId);

		// 특정 사용자가 해당 답글(Twit)을 삭제할 수 있는 권한을 확인합니다.
		if (!userId.equals(reply.getUser().getId())) {
			throw new UserException("You can't delete another user's reply");
		}

		// 답글(Twit)을 삭제합니다.
		twitRepository.deleteById(reply.getId());
	}

	@Override
	public List<Twit> findAllTwit() {
		// Sort sortByCreatedAtDesc = org.springframework.data.domain.Sort.Order("DESC")
		return twitRepository.findAllByIsTwitTrueOrderByCreatedAtDesc();
	}

	@Override
	public List<Twit> getUsersTwit(User user) {

		return twitRepository.findByRetwitUserContainsOrUser_IdAndIsTwitTrueOrderByCreatedAtDesc(user, user.getId());
	}
	
	@Override
	public List<Twit> getUsersRetwitTwit(User user) {

		return twitRepository.findByRetwitUserContainsOrUser_IdAndIsTwitTrueOrderByRetwitAtDesc(user, user.getId());
	}
	
	@Override
	public List<Twit> getUsersReplies(Long userId) {
		// TODO Auto-generated method stub
		System.out.println("reply check Service"+ userId);
		return twitRepository.findUsersReplies(userId);
	} 

	@Override
	public List<Twit> findByLikesContainsUser(User user) {
		return twitRepository.findByLikesUser_Id(user.getId());
	}

	@Override
	public Twit updateView(Twit twit) throws TwitException {
		// TODO Auto-generated method stub
		return twitRepository.save(twit);
	}
	
	@Override
	public List<Twit> searchTwit(String query) {
	
		return twitRepository.searchTwit(query);
	}

	@Override
	public List<Twit> findTwitFollowedByReqUser(User user) {
		// TODO Auto-generated method stub
		System.out.println(user.getId());
		return twitRepository.searchFollowedTwit(user.getId());
	}

	@Override
	public List<Twit> findTwitsByTopLike() throws UserException, TwitException {
		// TODO Auto-generated method stub
		return twitRepository.findTwitsByTopLike();
	}

	@Override
	public List<Twit> findTwitsByTopView() throws UserException, TwitException {
		// TODO Auto-generated method stub
		return twitRepository.findTwitsByTopView();
	}

	@Override
	public List<Twit> findTwitsByListId(Long listId) throws ListException, UserException, TwitException {
		// TODO Auto-generated method stub
		return twitRepository.searchListFollowedTwit(listId);
	}

	@Override
	public List<Twit> findTwitsByComId(Long comId) throws ComException {
		// TODO Auto-generated method stub
		return twitRepository.searchComFollowedTwit(comId);
	}
}
