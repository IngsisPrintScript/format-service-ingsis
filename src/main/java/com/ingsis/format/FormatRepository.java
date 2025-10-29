package com.ingsis.format;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormatRepository extends JpaRepository<Format, UUID> {
  List<Format> findByOwnerIdAndActive(String ownerId, boolean active);

  Format findByNameAndOwnerId(String name, String ownerId);

  Format findByOwnerIdAndId(String ownerId, UUID id);

    List<Format> findByOwnerId(String ownerId);
}
