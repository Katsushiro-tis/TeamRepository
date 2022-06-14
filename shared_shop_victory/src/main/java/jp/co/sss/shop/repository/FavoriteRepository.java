package jp.co.sss.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.entity.Item;


public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

	void save(Item item);

//	void save(Item item);
	

}
