package com.revature.controllers;

import com.revature.daos.AlbumDAO;
import com.revature.daos.UserDAO;
import com.revature.models.Album;
import com.revature.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    AlbumDAO albumDAO;
    UserDAO userDAO;

    @Autowired
    public AlbumController(AlbumDAO albumDAO, UserDAO userDAO) {
        this.albumDAO = albumDAO;
        this.userDAO = userDAO;
    }

    // add an album
    @PostMapping("/{userId}")
    public ResponseEntity<Album> addAlbum(@RequestBody Album album, @PathVariable int userId) {
        Optional<User> checkUser = userDAO.findById(userId);
        if (checkUser.isPresent()) {
            User user = checkUser.get();
            album.setUser(user);
            Album a = albumDAO.save(album);
            return ResponseEntity.status(201).body(a);
        }
        return ResponseEntity.internalServerError().build();
    }

    // update an album by id
    // patching partial change is not working
    @PatchMapping("/{albumId}")
    public ResponseEntity<Album> updateAlbum(@RequestBody Album album, @PathVariable int albumId) {
        if (albumId > 0) {
            Optional<Album> a = albumDAO.findById(albumId);

           if (a.isEmpty()) {
                return ResponseEntity.internalServerError().build();
            }
            albumDAO.save(album);
            return ResponseEntity.status(201).body(album);
        }
        return ResponseEntity.notFound().build();
    }

    // get all albums
    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() {
        List<Album> albums = albumDAO.findAll();
        return ResponseEntity.ok(albums);
    }

    // get an Album by id
    @GetMapping("/{albumId}")
    public ResponseEntity<Album> getAlbumById(@PathVariable int albumId) {
        if (albumId > 0) {
            Optional<Album> a = albumDAO.findById(albumId);
            if (a.isEmpty()) {
                return ResponseEntity.internalServerError().build();
            }
            return ResponseEntity.ok(a.get());
        }
        return ResponseEntity.notFound().build();
    }



    // get albums by userid

    //@GetMapping("/{authors/userid}")
    @GetMapping("/{userid}/albums")
    public ResponseEntity<List<Album>> getAlbumsByUserId(@PathVariable int userid) {
        List<Album> albums = albumDAO.findByUserUserId(userid);

        if (albums.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(albums);
    }

    // delete album by id
    @DeleteMapping("/{albumId}")
    public ResponseEntity<String> deleteAlbum(@PathVariable int albumId) {
        if (albumId > 0) {
            Optional<Album> a = albumDAO.findById(albumId);
            if (a.isPresent()) {
                albumDAO.deleteById(albumId);
                return ResponseEntity.ok("Deleted album");
            }
        }
        return ResponseEntity.notFound().build();
    }

}
