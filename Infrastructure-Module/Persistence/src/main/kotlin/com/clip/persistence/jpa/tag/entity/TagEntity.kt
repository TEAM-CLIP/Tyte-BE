package com.clip.persistence.jpa.tag.entity

import com.clip.persistence.jpa.common.AggregateRoot
import com.clip.persistence.jpa.common.EntityStatus
import com.clip.persistence.jpa.user.entity.UserEntity
import jakarta.persistence.*

@Entity
@Table(name = "tag")
class TagEntity(
    id: String,
    userId: String,
    name: String,
    color: String,
) : AggregateRoot<TagEntity>(id) {

    @Column(nullable = false)
    var name: String = name

    @Column(nullable = false)
    var color: String = color

    @Column(nullable = false)
    val userId: String = userId

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    lateinit var user: UserEntity

    @Enumerated(EnumType.STRING)
    @Column(
        name = "tag_status",
        nullable = false,
        columnDefinition = "varchar(20)",
    )
    var tagStatus: EntityStatus = EntityStatus.ACTIVE

}
