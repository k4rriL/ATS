<nav class="large-3 medium-4 columns" id="actions-sidebar">
    <ul class="side-nav">
        <li class="heading"><?= __('Actions') ?></li>
        <li><?= $this->Html->link(__('Edit Worker'), ['action' => 'edit', $worker->id]) ?> </li>
        <li><?= $this->Form->postLink(__('Delete Worker'), ['action' => 'delete', $worker->id], ['confirm' => __('Are you sure you want to delete # {0}?', $worker->id)]) ?> </li>
        <li><?= $this->Html->link(__('List Workers'), ['action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Worker'), ['action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List Roles'), ['controller' => 'Roles', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Role'), ['controller' => 'Roles', 'action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List Workers'), ['controller' => 'Workers', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Worker'), ['controller' => 'Workers', 'action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List Tickets'), ['controller' => 'Tickets', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Ticket'), ['controller' => 'Tickets', 'action' => 'add']) ?> </li>
    </ul>
</nav>
<div class="workers view large-9 medium-8 columns content">
    <h3><?= h($worker->name) ?></h3>
    <table class="vertical-table">
        <tr>
            <th scope="row"><?= __('Name') ?></th>
            <td><?= h($worker->name) ?></td>
        </tr>
        <tr>
            <th scope="row"><?= __('Email') ?></th>
            <td><?= h($worker->email) ?></td>
        </tr>
        <tr>
            <th scope="row"><?= __('Role') ?></th>
            <td><?= $worker->has('role') ? $this->Html->link($worker->role->id, ['controller' => 'Roles', 'action' => 'view', $worker->role->id]) : '' ?></td>
        </tr>
        <tr>
            <th scope="row"><?= __('Password') ?></th>
            <td><?= h($worker->password) ?></td>
        </tr>
        <tr>
            <th scope="row"><?= __('Id') ?></th>
            <td><?= $this->Number->format($worker->id) ?></td>
        </tr>
        <tr>
            <th scope="row"><?= __('Worker Id') ?></th>
            <td><?= $this->Number->format($worker->worker_id) ?></td>
        </tr>
    </table>
    <div class="related">
        <h4><?= __('Related Workers') ?></h4>
        <?php if (!empty($worker->workers)): ?>
        <table cellpadding="0" cellspacing="0">
            <tr>
                <th scope="col"><?= __('Id') ?></th>
                <th scope="col"><?= __('Name') ?></th>
                <th scope="col"><?= __('Email') ?></th>
                <th scope="col"><?= __('Worker Id') ?></th>
                <th scope="col"><?= __('Role Id') ?></th>
                <th scope="col"><?= __('Password') ?></th>
                <th scope="col" class="actions"><?= __('Actions') ?></th>
            </tr>
            <?php foreach ($worker->workers as $workers): ?>
            <tr>
                <td><?= h($workers->id) ?></td>
                <td><?= h($workers->name) ?></td>
                <td><?= h($workers->email) ?></td>
                <td><?= h($workers->worker_id) ?></td>
                <td><?= h($workers->role_id) ?></td>
                <td><?= h($workers->password) ?></td>
                <td class="actions">
                    <?= $this->Html->link(__('View'), ['controller' => 'Workers', 'action' => 'view', $workers->id]) ?>
                    <?= $this->Html->link(__('Edit'), ['controller' => 'Workers', 'action' => 'edit', $workers->id]) ?>
                    <?= $this->Form->postLink(__('Delete'), ['controller' => 'Workers', 'action' => 'delete', $workers->id], ['confirm' => __('Are you sure you want to delete # {0}?', $workers->id)]) ?>
                </td>
            </tr>
            <?php endforeach; ?>
        </table>
        <?php endif; ?>
    </div>
    <div class="related">
        <h4><?= __('Related Tickets') ?></h4>
        <?php if (!empty($worker->tickets)): ?>
        <table cellpadding="0" cellspacing="0">
            <tr>
                <th scope="col"><?= __('Id') ?></th>
                <th scope="col"><?= __('Activity Id') ?></th>
                <th scope="col"><?= __('Parent Id') ?></th>
                <th scope="col" class="actions"><?= __('Actions') ?></th>
            </tr>
            <?php foreach ($worker->tickets as $tickets): ?>
            <tr>
                <td><?= h($tickets->id) ?></td>
                <td><?= h($tickets->activity_id) ?></td>
                <td><?= h($tickets->parent_id) ?></td>
                <td class="actions">
                    <?= $this->Html->link(__('View'), ['controller' => 'Tickets', 'action' => 'view', $tickets->id]) ?>
                    <?= $this->Html->link(__('Edit'), ['controller' => 'Tickets', 'action' => 'edit', $tickets->id]) ?>
                    <?= $this->Form->postLink(__('Delete'), ['controller' => 'Tickets', 'action' => 'delete', $tickets->id], ['confirm' => __('Are you sure you want to delete # {0}?', $tickets->id)]) ?>
                </td>
            </tr>
            <?php endforeach; ?>
        </table>
        <?php endif; ?>
    </div>
</div>
