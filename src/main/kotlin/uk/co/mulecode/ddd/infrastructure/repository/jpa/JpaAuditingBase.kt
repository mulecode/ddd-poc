package uk.co.mulecode.ddd.infrastructure.repository.jpa

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Version
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class JpaAuditingBase {

    @CreatedBy
    @Column(name = "created_by", nullable = true, updatable = false)
    var createdBy: String? = null

    @CreatedDate
    @Column(name = "created_date", nullable = true, updatable = false)
    var createdDate: LocalDateTime? = null

    @LastModifiedBy
    @Column(name = "last_modified_by", nullable = true)
    var lastModifiedBy: String? = null

    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = true)
    var lastModifiedDate: LocalDateTime? = null

    @Version
    @Column(name = "version", nullable = false)
    val version: Int = 0

//    @PrePersist
//    fun prePersist() {
//        println("PrePersist: createdBy=$createdBy, createdAt=$createdDate")
//    }
//
//    @PreUpdate
//    fun preUpdate() {
//        println("PreUpdate: updatedBy=$lastModifiedBy, updatedAt=$lastModifiedDate")
//    }
}
