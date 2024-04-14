package com.revature.controllers;

import com.revature.daos.AlbumDAO;
import com.revature.models.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    AlbumDAO albumDAO;

    @Autowired
    public AlbumController(AlbumDAO albumDAO) {
        this.albumDAO = albumDAO;
    }

    // add an album
    @PostMapping
    public ResponseEntity<Album> addAlbum(@RequestBody Album album) {
        Album a = albumDAO.save(album);

        if (a == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.status(201).body(a);
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
    /*
    @GetMapping("/{Id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable int albumId) {
        if (albumId > 0) {
            Optional<Album> a = albumDAO.findById(albumId);
            if (a.isEmpty()) {
                return ResponseEntity.internalServerError().build();
            }
            return ResponseEntity.ok(a);
        }
        return ResponseEntity.notFound().build();
    }
    */


    // get albums by userid
    /*
    @GetMapping("/{authors/userid}")
    public ResponseEntity<List<Album>> getAlbumsByUserId(@PathVariable int userid) {
        List<Album> albums = albumDAO.findBbUserId(userid);

        if (albums == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.status(201).body(albums);
    }
    */
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
