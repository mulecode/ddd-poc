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
import java.time.Instant

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class JpaAuditingBase {

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    var createdBy: String? = null

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    var createdDate: Instant? = null

    @LastModifiedBy
    @Column(name = "last_modified_by", nullable = false, updatable = true)
    var lastModifiedBy: String? = null

    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = false, updatable = true)
    var lastModifiedDate: Instant? = null

    @Version
    @Column(name = "version", nullable = false, updatable = true)
    val version: Int = 0

}
