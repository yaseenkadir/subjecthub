import { Component, Input } from '@angular/core';

import {AddTagService} from '../services/add-tag.service';
import {Tag} from "../models/Tag";
import { Utils } from '../utils/utils';

@Component({
  selector: 'app-add-tag',
  templateUrl: './add-tag.component.html',
  styleUrls: ['./add-tag.component.css'],
    providers:[AddTagService]
})
export class AddTagComponent {
  @Input()
  universityId: number;

  @Input()
  subjectId: number;

  error: string;
  success: string;
  tag: Tag;
  isLoading: boolean;

  constructor(private tagService: AddTagService) {
    this.tag = new Tag('');
    this.isLoading = false;
  }
  clearMessages() {

      this.error = '';
      this.success = '';
  }

  clearField() {
      this.isLoading = false;
      this.tag.name = '';
  }

  submit() {
      this.clearMessages();
      if (!this.tag.name) this.error = "A Tag Name must be supplied";
      else {
          this.isLoading = true;
          setTimeout(() => {
            this.tagService.createTag(this.universityId, this.subjectId, this.tag)
              .then(subject => {
                  this.success = "Tag added successfully";
                  this.clearField();
              })
              .catch(err => {
                  this.error = Utils.getApiErrorMessage(err);
                  this.clearField();
              })
          }, 1000);

      }
  }


}
