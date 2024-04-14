package com.revature.daos;

import com.revature.models.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumDAO extends JpaRepository<Album, Integer> {


    //public List<Album> albums getAlbumsByUserId(int userid);

}
