<nav class="large-3 medium-4 columns" id="actions-sidebar">
    <ul class="side-nav">
        <li class="heading"><?= __('Actions') ?></li>
        <li><?= $this->Html->link(__('Edit Ticket'), ['action' => 'edit', $ticket->id]) ?> </li>
        <li><?= $this->Form->postLink(__('Delete Ticket'), ['action' => 'delete', $ticket->id], ['confirm' => __('Are you sure you want to delete # {0}?', $ticket->id)]) ?> </li>
        <li><?= $this->Html->link(__('List Tickets'), ['action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Ticket'), ['action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List Activities'), ['controller' => 'Activities', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Activity'), ['controller' => 'Activities', 'action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List Parent Tickets'), ['controller' => 'Tickets', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Parent Ticket'), ['controller' => 'Tickets', 'action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List Workers'), ['controller' => 'Workers', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Worker'), ['controller' => 'Workers', 'action' => 'add']) ?> </li>
    </ul>
</nav>
<div class="tickets view large-9 medium-8 columns content">
    <h3><?= h($ticket->id) ?></h3>
    <table class="vertical-table">
        <tr>
            <th scope="row"><?= __('Activity') ?></th>
            <td><?= $ticket->has('activity') ? $this->Html->link($ticket->activity->id, ['controller' => 'Activities', 'action' => 'view', $ticket->activity->id]) : '' ?></td>
        </tr>
        <tr>
            <th scope="row"><?= __('Parent Ticket') ?></th>
            <td><?= $ticket->has('parent_ticket') ? $this->Html->link($ticket->parent_ticket->id, ['controller' => 'Tickets', 'action' => 'view', $ticket->parent_ticket->id]) : '' ?></td>
        </tr>
        <tr>
            <th scope="row"><?= __('Id') ?></th>
            <td><?= $this->Number->format($ticket->id) ?></td>
        </tr>
    </table>
    <div class="row">
        <h4><?= __('Description') ?></h4>
        <?= $this->Text->autoParagraph(h($ticket->description)); ?>
    </div>
    <div class="related">
        <h4><?= __('Related Tickets') ?></h4>
        <?php if (!empty($ticket->child_tickets)): ?>
        <table cellpadding="0" cellspacing="0">
            <tr>
                <th scope="col"><?= __('Id') ?></th>
                <th scope="col"><?= __('Activity Id') ?></th>
                <th scope="col"><?= __('Parent Id') ?></th>
                <th scope="col"><?= __('Description') ?></th>
                <th scope="col" class="actions"><?= __('Actions') ?></th>
            </tr>
            <?php foreach ($ticket->child_tickets as $childTickets): ?>
            <tr>
                <td><?= h($childTickets->id) ?></td>
                <td><?= h($childTickets->activity_id) ?></td>
                <td><?= h($childTickets->parent_id) ?></td>
                <td><?= h($childTickets->description) ?></td>
                <td class="actions">
                    <?= $this->Html->link(__('View'), ['controller' => 'Tickets', 'action' => 'view', $childTickets->id]) ?>
                    <?= $this->Html->link(__('Edit'), ['controller' => 'Tickets', 'action' => 'edit', $childTickets->id]) ?>
                    <?= $this->Form->postLink(__('Delete'), ['controller' => 'Tickets', 'action' => 'delete', $childTickets->id], ['confirm' => __('Are you sure you want to delete # {0}?', $childTickets->id)]) ?>
                </td>
            </tr>
            <?php endforeach; ?>
        </table>
        <?php endif; ?>
    </div>
    <div class="related">
        <h4><?= __('Related Workers') ?></h4>
        <?php if (!empty($ticket->workers)): ?>
        <table cellpadding="0" cellspacing="0">
            <tr>
                <th scope="col"><?= __('Id') ?></th>
                <th scope="col"><?= __('Name') ?></th>
                <th scope="col"><?= __('Email') ?></th>
                <th scope="col"><?= __('Worker Id') ?></th>
                <th scope="col"><?= __('Role Id') ?></th>
                <th scope="col" class="actions"><?= __('Actions') ?></th>
            </tr>
            <?php foreach ($ticket->workers as $workers): ?>
            <tr>
                <td><?= h($workers->id) ?></td>
                <td><?= h($workers->name) ?></td>
                <td><?= h($workers->email) ?></td>
                <td><?= h($workers->worker_id) ?></td>
                <td><?= h($workers->role_id) ?></td>
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
</div>
