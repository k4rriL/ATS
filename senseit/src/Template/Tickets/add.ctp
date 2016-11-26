<nav class="large-3 medium-4 columns" id="actions-sidebar">
    <ul class="side-nav">
        <li class="heading"><?= __('Actions') ?></li>
        <li><?= $this->Html->link(__('List Tickets'), ['action' => 'index']) ?></li>
        <li><?= $this->Html->link(__('List Activities'), ['controller' => 'Activities', 'action' => 'index']) ?></li>
        <li><?= $this->Html->link(__('New Activity'), ['controller' => 'Activities', 'action' => 'add']) ?></li>
        <li><?= $this->Html->link(__('List Parent Tickets'), ['controller' => 'Tickets', 'action' => 'index']) ?></li>
        <li><?= $this->Html->link(__('New Parent Ticket'), ['controller' => 'Tickets', 'action' => 'add']) ?></li>
        <li><?= $this->Html->link(__('List Workers'), ['controller' => 'Workers', 'action' => 'index']) ?></li>
        <li><?= $this->Html->link(__('New Worker'), ['controller' => 'Workers', 'action' => 'add']) ?></li>
    </ul>
</nav>
<div class="tickets form large-9 medium-8 columns content">
    <?= $this->Form->create($ticket) ?>
    <fieldset>
        <legend><?= __('Add Ticket') ?></legend>
        <?php
            echo $this->Form->input('activity_id', ['options' => $activities]);
            echo $this->Form->input('parent_id', ['options' => $parentTickets, 'empty' => true]);
            echo $this->Form->input('workers._ids', ['options' => $workers]);
        ?>
    </fieldset>
    <?= $this->Form->button(__('Submit')) ?>
    <?= $this->Form->end() ?>
</div>
