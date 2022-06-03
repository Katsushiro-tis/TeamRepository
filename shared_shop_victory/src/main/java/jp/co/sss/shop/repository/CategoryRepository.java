package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Category;

/**
 * categoriesテーブル用リポジトリ
 *
 * @author System Shared
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Category findByNameAndDeleteFlag(String name, int deleteFlag);

	Category findByIdAndDeleteFlag(Integer id, int deleteFlag);

	// カテゴリ情報を登録日付順に取得
	List<Category> findByDeleteFlagOrderByInsertDateDescIdAsc(int deleteFlag);

}
