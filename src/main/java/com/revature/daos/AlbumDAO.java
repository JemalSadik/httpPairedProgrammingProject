package com.revature.daos;

import com.revature.models.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumDAO extends JpaRepository<Album, Integer> {

    public List<Album> findByUserUserId(int userId);
    //public List<Album> getAlbumsByUserId(int userid);

}
