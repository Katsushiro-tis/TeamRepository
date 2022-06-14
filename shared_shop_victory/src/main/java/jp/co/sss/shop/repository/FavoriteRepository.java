package jp.co.sss.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.shop.entity.Favorite;


public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
	

}
