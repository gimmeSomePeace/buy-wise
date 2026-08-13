package me.gimmesomepeace.buywise.infrastructure.persistence.user

import me.gimmesomepeace.buywise.application.user.UserListItem
import me.gimmesomepeace.buywise.application.user.UserView
import me.gimmesomepeace.buywise.domain.shared.password.PasswordHash
import me.gimmesomepeace.buywise.domain.user.Login
import me.gimmesomepeace.buywise.domain.user.User
import me.gimmesomepeace.buywise.domain.user.UserId

fun UserEntity.toDomain() = User(
    id = UserId(this.id),
    login = Login(this.login),
    passwordHash = PasswordHash(this.passwordHash),
    role = this.role,
)

fun User.toEntity() = UserEntity(
    id = this.id.value,
    login = this.login.value,
    passwordHash = this.passwordHash.value,
    role = this.role
)

fun UserEntity.toView() = UserView(
    id = UserId(this.id),
    login = Login(this.login),
    passwordHash = PasswordHash(this.passwordHash),
    role = this.role,
)

fun UserEntity.toListItem() = UserListItem(
    id = UserId(this.id),
    login = Login(this.login),
    role = this.role,
)
