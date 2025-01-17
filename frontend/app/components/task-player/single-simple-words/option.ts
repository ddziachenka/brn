import Component from '@glimmer/component';
import { action } from '@ember/object';
import { MODES } from 'brn/utils/task-modes';

interface ITaskPlayerSingleWordsOptionComponentArguments {
  mode: keyof typeof MODES;
  disableAnswers: boolean;
  isCorrect: boolean;
  activeWord: string;
}
export default class TaskPlayerSingleWordsOptionComponent extends Component<ITaskPlayerSingleWordsOptionComponentArguments> {
  isClicked = false;
  @action setDefaultImage(e: Error & { target: HTMLImageElement}) {
    e.target.src = 'data:image/gif;base64,R0lGODlhAQABAPAAAAAAAAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==';
  }
  @action handleClick(cb: any) {
    this.isClicked = true;
    cb();
  }
  get isDisabled() {
    return this.args.disableAnswers || this.args.mode === MODES.LISTEN || false;
  }
  @action handleAnswer(node: HTMLButtonElement, [isCorrect]: [boolean]) {
    if (this.args.mode !== MODES.TASK) {
      return;
    }
    if (this.isClicked) {
      if (isCorrect) {
        node.style.backgroundColor = '#47CD8A';
        node.style.color = '#fff';
      } else {
        node.style.backgroundColor = '#F38698';
        node.style.color = '#fff';
      }
      this.isClicked = false;
    } else {
      node.setAttribute('style', '');
    }
  }
}
