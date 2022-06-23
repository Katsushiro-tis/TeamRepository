package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.User;


public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
	
	public List<Favorite> findByUser(User user);
	List<Favorite> findByItemAndUser(Item item, User user);

}
