import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { User, UserRole } from '../../../models/user.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-admin-users',
  templateUrl: './admin-users.component.html',
  styleUrls: ['./admin-users.component.scss']
})
export class AdminUsersComponent implements OnInit {
  users: User[] = [];
  loading = true;
  error = '';

  userForm: FormGroup;
  editingUser: User | null = null;
  modalRef: NgbModalRef | null = null;
  userRoles = Object.values(UserRole);

  constructor(
    private userService: UserService,
    private fb: FormBuilder,
    private modalService: NgbModal
  ) {
    this.userForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', Validators.required],
      role: [UserRole.USER, Validators.required],
      isActive: [true],
      isVerified: [false]
    });
  }

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.loading = true;
    this.userService.getAllUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load users.';
        this.loading = false;
        console.error('Error loading users:', error);
      }
    });
  }

  openEditUser(user: User, modalContent: any) {
    this.editingUser = user;
    this.userForm.patchValue({
      name: user.name,
      email: user.email,
      phoneNumber: user.phoneNumber,
      role: user.role,
      isActive: user.active,
      isVerified: user.verified
    });
    this.modalRef = this.modalService.open(modalContent);
  }

  submitUser() {
    if (this.userForm.invalid) return;
    const userData = this.userForm.value;
    
    console.log('User data being sent:', userData);
    
    if (this.editingUser) {
      this.userService.updateUser(this.editingUser.id!, userData).subscribe({
        next: () => {
          this.loadUsers();
          this.modalRef?.close();
        },
        error: (error) => {
          this.error = 'Failed to update user.';
          console.error('Error updating user:', error);
        }
      });
    }
  }

  toggleUserStatus(user: User) {
    const action = user.active ? 'disable' : 'enable';
    if (confirm(`Are you sure you want to ${action} this user?`)) {
      const serviceCall = user.active
        ? this.userService.disableUser(user.id!)
        : this.userService.enableUser(user.id!);

      serviceCall.subscribe({
        next: () => {
          user.active = !user.active;
        },
        error: (error) => {
          this.error = `Failed to ${action} user.`;
          console.error(`Error ${action}ing user:`, error);
        }
      });
    }
  }

  deleteUser(userId: number) {
    if (confirm('Are you sure you want to delete this user? This action cannot be undone.')) {
      this.userService.deleteUser(userId).subscribe({
        next: () => {
          this.users = this.users.filter(user => user.id !== userId);
        },
        error: (error) => {
          this.error = 'Failed to delete user.';
          console.error('Error deleting user:', error);
        }
      });
    }
  }

  getRoleDisplayName(role: UserRole): string {
    return role === UserRole.ADMIN ? 'Administrator' : 'User';
  }
} 